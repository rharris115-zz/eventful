package uk.co.maximumlikelihood.eventful.util;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public final class Functions {

    private Functions() {
    }

    public static <T, U, R> Function<T, R> ofBiFunctionsFirstArgument(BiFunction<T, U, R> biFunction, Supplier<U> uSupplier) {
        return (t) -> biFunction.apply(t, uSupplier.get());
    }

    public static <T, U, R> Function<U, R> ofBiFunctionsSecondArgument(BiFunction<T, U, R> biFunction, Supplier<T> tSupplier) {
        return (u) -> biFunction.apply(tSupplier.get(), u);
    }
}
