package uk.co.maximumlikelihood.eventful.util;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public final class Functions {

    private Functions() {
    }

    public static <T, U, R> Function<T, R> ofBiFunctionsFirstArgument(BiFunction<T, U, R> bFunction, Supplier<U> uSupplier) {
        return (t) -> bFunction.apply(t, uSupplier.get());
    }

    public static <T, U, R> Function<U, R> ofBiFunctionsSecondArgument(BiFunction<T, U, R> bFunction, Supplier<T> tSupplier) {
        return (u) -> bFunction.apply(tSupplier.get(), u);
    }
}
