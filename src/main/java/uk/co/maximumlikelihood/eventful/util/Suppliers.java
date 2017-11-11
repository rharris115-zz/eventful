package uk.co.maximumlikelihood.eventful.util;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public final class Suppliers {
    private Suppliers() {
    }

    public static <T, R> Supplier<R> ofFunction(Function<T, R> function,
                                                Supplier<T> tSupplier) {
        return () -> function.apply(tSupplier.get());
    }

    public static <T, U, R> Supplier<R> ofBiFunction(BiFunction<T, U, R> biFunction,
                                                     Supplier<T> tSupplier,
                                                     Supplier<U> uSupplier) {
        return () -> biFunction.apply(tSupplier.get(), uSupplier.get());
    }
}
