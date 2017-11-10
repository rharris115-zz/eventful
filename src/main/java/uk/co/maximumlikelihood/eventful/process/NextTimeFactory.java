package uk.co.maximumlikelihood.eventful.process;


import java.time.LocalDateTime;
import java.time.temporal.TemporalUnit;
import java.util.function.Function;
import java.util.function.Supplier;

public final class NextTimeFactory {
    private NextTimeFactory() {
    }

    public static Function<LocalDateTime, LocalDateTime> inUnits(Supplier<Double> elapsedTimeSupplier, TemporalUnit unit) {
        final long unitNanos = unit.getDuration().getNano() + unit.getDuration().getSeconds() * 1_000_000_000L;
        return t -> t.plusNanos((long) Math.ceil(elapsedTimeSupplier.get() * unitNanos));
    }
}