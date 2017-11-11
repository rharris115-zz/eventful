package uk.co.maximumlikelihood.eventful.examples;

import org.apache.commons.math3.distribution.ExponentialDistribution;
import uk.co.maximumlikelihood.eventful.event.EventTask;
import uk.co.maximumlikelihood.eventful.event.FutureEventsQueue;
import uk.co.maximumlikelihood.eventful.process.SimpleRecurringEventProcess;
import uk.co.maximumlikelihood.eventful.queue.CapacitatedEntityQueuePostProcessor;
import uk.co.maximumlikelihood.eventful.queue.EntityQueue;
import uk.co.maximumlikelihood.eventful.queue.QueueArrivalTask;
import uk.co.maximumlikelihood.eventful.queue.QueueableEntity;
import uk.co.maximumlikelihood.eventful.util.ElapsedTimeFactory;
import uk.co.maximumlikelihood.eventful.util.Suppliers;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.function.Function;
import java.util.function.Supplier;

public class BankTeller {

    private static class Customer implements QueueableEntity<Customer, LocalDateTime> {

        private static int count = 0;

        private int id = ++count;

        @Override
        public String toString() {
            return String.format("Customer: %d", id);
        }

        @Override
        public void notifyQueueArrival(EntityQueue<Customer, LocalDateTime> queue, LocalDateTime time) {
            System.out.printf("%s arriving at queue @t=%s\n", this, time);
        }

        @Override
        public void notifyEnterringQueue(EntityQueue<Customer, LocalDateTime> queue, LocalDateTime time) {
            System.out.printf("%s enterring queue @t=%s\n", this, time);
        }

        @Override
        public void notifyQueuePostProcessingStart(EntityQueue<Customer, LocalDateTime> queue, LocalDateTime time) {
            System.out.printf("%s started being serviced @t=%s\n", this, time);
        }

        @Override
        public void notifyQueuePostProcessingFinish(EntityQueue<Customer, LocalDateTime> queue, LocalDateTime time) {
            System.out.printf("%s finished being serviced @t=%s\n", this, time);
        }
    }


    public static void main(String[] args) {
        final FutureEventsQueue<LocalDateTime> futureEvents = FutureEventsQueue.starting(LocalDateTime.now());


        final CapacitatedEntityQueuePostProcessor<Customer, LocalDateTime> queueProcessor
                = new CapacitatedEntityQueuePostProcessor<>(ElapsedTimeFactory.withSupplierInUnits(new ExponentialDistribution(20.0)::sample, ChronoUnit.SECONDS),
                () -> fe -> {
                },
                2);

        final EntityQueue<Customer, LocalDateTime> queue = new EntityQueue<>(queueProcessor);

        final Supplier<EventTask<LocalDateTime>> arrivals
                = Suppliers.ofBiFunction(QueueArrivalTask::new, Customer::new, Suppliers.ofConstant(queue));

        final Function<LocalDateTime, LocalDateTime> arrivalTimes
                = ElapsedTimeFactory.withSupplierInUnits(new ExponentialDistribution(10.0)::sample,
                ChronoUnit.SECONDS);

        final SimpleRecurringEventProcess<LocalDateTime> customerArrivalProcess
                = new SimpleRecurringEventProcess<>(arrivals, arrivalTimes);

        final LocalDateTime end = LocalDateTime.now().plusDays(1);

        customerArrivalProcess.scheduleNext(futureEvents);

        while (futureEvents.hasNext() && futureEvents.getCurrentTime().isBefore(end)) {
            futureEvents.next();
        }
    }
}
