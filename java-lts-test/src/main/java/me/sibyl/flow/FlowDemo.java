package me.sibyl.flow;

import lombok.SneakyThrows;

import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * @author dyingleaf3213
 * @Classname FlowDemo
 * @Description TODO
 * @Create 2023/06/12 21:35
 */

public class FlowDemo {

    @SneakyThrows
    public static void main(String[] args) {

        TestSubscriber<String> subscriber = new TestSubscriber();//订阅者
        SubmissionPublisher<String> publisher = new SubmissionPublisher();//发布者
        publisher.subscribe(subscriber);
        for (int i = 0; i < 500; i++) {
            String msg = String.valueOf(i);
            System.err.println("publisher => " + msg);
            publisher.submit(msg);
        }

        publisher.close();

        Thread.currentThread().join();
    }

    /**
     * 订阅者
     */
    static class TestSubscriber<String> implements Flow.Subscriber {
        static final long bufferSize = 3;
        Flow.Subscription subscription;

        @Override
        public void onSubscribe(Flow.Subscription subscription) {
            System.err.println("onSubscribe");
            System.err.println(subscription);
            this.subscription = subscription;
            this.subscription.request(1);
        }

        @Override
        public void onNext(Object item) {
            System.err.println("subscriber => " + item);

            this.subscription.request(1);
//            this.subscription.cancel();
            System.err.println();
        }

        @Override
        public void onError(Throwable throwable) {
            System.err.println("onError");
            throwable.printStackTrace();
            this.subscription.cancel();
        }

        @Override
        public void onComplete() {
            System.err.println("onComplete");
        }
    }
}
