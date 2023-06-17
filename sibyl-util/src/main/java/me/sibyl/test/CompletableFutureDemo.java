package me.sibyl.test;

import lombok.SneakyThrows;
import me.sibyl.util.thread.ThreadUtil;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author dyingleaf3213
 * @Classname CompletableFutureDemo
 * @Description TODO
 * @Create 2023/06/13 20:47
 */

public class CompletableFutureDemo {

    /**
     *   supplyAsync -> 有返回值
     *   runAsync -> 无返回值
     */
    public static void main(String[] args) {
//        applyToEither();
//        acceptEither();
//        runAfterEither();
//        allOf();
        anyOf();

        //whenComplete
        //exceptionally
    }

    @SneakyThrows
    private static void anyOf() {
        CompletableFuture<String>[] tasks = Stream.of(2, 4, 1, 3, 5)
                .map(i -> CompletableFuture.supplyAsync(() -> {
                    System.err.println(Thread.currentThread().getName());
                    ThreadUtil.sleep(i * 100);
                    return String.valueOf(i * 100);
                })).toArray(CompletableFuture[]::new);

        long start = System.currentTimeMillis();
        CompletableFuture<String> future = ThreadUtil.anyOf(tasks);
        String s = future.get();
        System.err.println(System.currentTimeMillis() - start);
        System.err.println(s);
        System.err.println(s.getClass());

//        String collect = Arrays.stream(tasks).map(t -> {
//            try {
//                return t.get();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            } catch (ExecutionException e) {
//                e.printStackTrace();
//            }
//            return null;
//        }).collect(Collectors.joining(","));
//        System.err.println(collect);
    }

    @SneakyThrows
    private static void allOf() {
        CompletableFuture<String>[] tasks = Stream.of(1, 2, 3, 4, 5)
                .map(i -> CompletableFuture.supplyAsync(() -> {
                    System.err.println(Thread.currentThread().getName());
                    ThreadUtil.sleep(i * 100);
                    return String.valueOf(i * 100);
                })).toArray(CompletableFuture[]::new);
        CompletableFuture<Void> allOf = CompletableFuture.allOf(tasks);
        long start = System.currentTimeMillis();
        allOf.get();
        System.err.println(System.currentTimeMillis() - start);

        allOf.thenApply(v -> {
            System.err.println(v);
            System.err.println(Arrays.stream(tasks).map(ThreadUtil::get).collect(Collectors.toList()));
            return "end";
        }).join();

//        String collect = Arrays.stream(tasks).map(t -> {
//            try {
//                return t.get();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            } catch (ExecutionException e) {
//                e.printStackTrace();
//            }
//            return null;
//        }).collect(Collectors.joining(","));
//        System.err.println(collect);
    }

    @SneakyThrows
    private static void runAfterEither() {
        CompletableFuture<Void> task = CompletableFuture.supplyAsync(() -> {
            System.err.println(Thread.currentThread().getName());
            ThreadUtil.sleep(100);
            return "supplyAsync100";
        }).runAfterEither(CompletableFuture.supplyAsync(() -> {
            System.err.println(Thread.currentThread().getName());
            ThreadUtil.sleep(50);
            return "supplyAsync50";
        }), () -> {
            System.err.println("fin end");
            System.err.println(Thread.currentThread().getName());
        });

        long start = System.currentTimeMillis();
        task.get();
        System.err.println(System.currentTimeMillis() - start);
    }

    @SneakyThrows
    private static void acceptEither() {
        CompletableFuture<Void> task = CompletableFuture.supplyAsync(() -> {
            System.err.println(Thread.currentThread().getName());
            ThreadUtil.sleep(100);
            return "supplyAsync100";
        }).acceptEither(CompletableFuture.supplyAsync(() -> {
            System.err.println(Thread.currentThread().getName());
            ThreadUtil.sleep(50);
            return "supplyAsync50";
        }), r -> System.err.println(r));

        long start = System.currentTimeMillis();
        task.get();
        System.err.println(System.currentTimeMillis() - start);
    }

    @SneakyThrows
    private static void applyToEither() {
        CompletableFuture<String> task = CompletableFuture.supplyAsync(() -> {
            System.err.println(Thread.currentThread().getName());
            ThreadUtil.sleep(100);
            return "supplyAsync100";
        }).applyToEither(CompletableFuture.supplyAsync(() -> {
            System.err.println(Thread.currentThread().getName());
            ThreadUtil.sleep(50);
            return "supplyAsync50";
        }), r -> r);

        long start = System.currentTimeMillis();
        String s = task.get();
        System.err.println(s);
        System.err.println(System.currentTimeMillis() - start);
    }

    @SneakyThrows
    public static void runAfterBoth() {
        CompletableFuture<Void> taskAsync = CompletableFuture.supplyAsync(() -> {
            System.err.println(Thread.currentThread().getName());
            ThreadUtil.sleep(100);
            return "supplyAsync100";
        }).runAfterBoth(CompletableFuture.supplyAsync(() -> {
            System.err.println(Thread.currentThread().getName());
            ThreadUtil.sleep(50);
            return " and thenCombine";
        }), () -> System.err.println("run end"));

        long start = System.currentTimeMillis();
        taskAsync.get();
        System.err.println(System.currentTimeMillis() - start);

    }

    @SneakyThrows
    public static void thenAcceptBoth() {
        CompletableFuture<Void> taskAsync = CompletableFuture.supplyAsync(() -> {
            System.err.println(Thread.currentThread().getName());
            ThreadUtil.sleep(100);
            return "supplyAsync100";
        }).thenAcceptBoth(CompletableFuture.supplyAsync(() -> {
            System.err.println(Thread.currentThread().getName());
            ThreadUtil.sleep(50);
            return " and thenCombine";
        }), (l, r) -> {
            System.err.println("fin ->");
            System.err.println(l + r);
        });

        long start = System.currentTimeMillis();
        taskAsync.get();
        System.err.println(System.currentTimeMillis() - start);

        System.err.println();
    }

    @SneakyThrows
    public static void thenCombine() {
        CompletableFuture<String> taskAsync = CompletableFuture.supplyAsync(() -> {
            System.err.println(Thread.currentThread().getName());
            ThreadUtil.sleep(100);
            return "supplyAsync100";
        }).thenCombine(CompletableFuture.supplyAsync(() -> {
            System.err.println(Thread.currentThread().getName());
            ThreadUtil.sleep(50);
            return " and thenCombine";
        }), (l, r) -> l + r);

        long start = System.currentTimeMillis();
        String getTask100 = taskAsync.get();
        System.err.println(getTask100);
        System.err.println(System.currentTimeMillis() - start);

        System.err.println();
    }

    @SneakyThrows
    public static void thenCompose() {
        CompletableFuture<String> taskAsync = CompletableFuture.supplyAsync(() -> {
            System.err.println(Thread.currentThread().getName());
            ThreadUtil.sleep(100);
            return "supplyAsync100";
            // thenComposeAsync
        }).thenCompose(task -> CompletableFuture.supplyAsync(() -> {
            System.err.println(Thread.currentThread().getName());
            ThreadUtil.sleep(50);
            return task + " => thenCompose";
        }));

        long start = System.currentTimeMillis();
        String getTask100 = taskAsync.get();
        System.err.println(getTask100);
        System.err.println(System.currentTimeMillis() - start);

        System.err.println();
    }

    @SneakyThrows
    public static void thenApply() {
        CompletableFuture<String> task100 = CompletableFuture.supplyAsync(() -> {
            System.err.println(Thread.currentThread().getName());
            ThreadUtil.sleep(100);
            return "supplyAsync100";
        }).thenApplyAsync(task -> task + " => thenApply");
//        }).thenApply(task -> task + " => thenApply");

        long start = System.currentTimeMillis();
        String getTask100 = task100.get();
        System.err.println(getTask100);
        System.err.println(System.currentTimeMillis() - start);
        System.err.println();
    }
}
