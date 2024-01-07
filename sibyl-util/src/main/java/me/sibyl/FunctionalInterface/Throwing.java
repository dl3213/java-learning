package me.sibyl.FunctionalInterface;

import java.util.function.Consumer;

public class Throwing {
    private Throwing() {}


    public static <T> Consumer<T> rethrow( final ThrowingConsumer<T> consumer) {
        return consumer;
    }

    /**
     * The compiler sees the signature with the throws T inferred to a RuntimeException type, so it allows the unchecked
     * exception to propagate.
     *
     * http://www.baeldung.com/java-sneaky-throws
     */
    @SuppressWarnings("unchecked")

    public static <E extends Throwable> void sneakyThrow( final Throwable ex) throws E {
        throw (E)ex;
    }
}