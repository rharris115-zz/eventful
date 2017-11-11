package uk.co.maximumlikelihood.eventful.util;


import java.time.LocalDateTime;
import java.time.temporal.TemporalUnit;
import java.util.function.Function;
import java.util.function.Supplier;

public final class ElapsedTimeFactory {
    private ElapsedTimeFactory() {
    }

    public static Function<LocalDateTime, LocalDateTime> withSupplierInUnits(Supplier<Double> elapsedTimeSupplier, TemporalUnit unit) {
        final long unitNanos = unit.getDuration().getNano() + unit.getDuration().getSeconds() * 1_000_000_000L;
        return t -> t.plusNanos((long) Math.ceil(elapsedTimeSupplier.get() * unitNanos));
    }
}
