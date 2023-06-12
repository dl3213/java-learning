package me.sibyl.flow;

import lombok.SneakyThrows;

import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;

/**
 * @author dyingleaf3213
 * @Classname ProcessorDemo
 * @Description https://mrbird.cc/Java-9-Flow-API-Learn.html
 * @Create 2023/06/12 22:16
 */

public class ProcessorDemo {

    @SneakyThrows
    public static void main(String[] args) {
        SubmissionPublisher<String> publisher = new SubmissionPublisher<>();
        TestProcessor processor = new TestProcessor();
        publisher.subscribe(processor);

        Flow.Subscriber<String> subscriber = new Flow.Subscriber<>() {
            private Flow.Subscription subscription;

            @Override
            public void onSubscribe(Flow.Subscription subscription) {
                this.subscription = subscription;
                this.subscription.request(1);
            }

            @Override
            public void onNext(String item) {
                System.err.println("【订阅者】接收消息 <------ " + item + "");
                this.subscription.request(1);
            }

            @Override
            public void onError(Throwable throwable) {
                System.err.println("【订阅者】数据接收出现异常，" + throwable);
                this.subscription.cancel();
            }

            @Override
            public void onComplete() {
                System.err.println("【订阅者】数据接收完毕");
            }
        };

        processor.subscribe(subscriber);

        for (int i = 0; i < 10; i++) {
            String message = String.valueOf(i);
            System.err.println("【发布者】发布消息 ------> " + message);
            publisher.submit(message);
        }
        publisher.close();

        Thread.currentThread().join(20000);
    }

    static class TestProcessor extends SubmissionPublisher<String> implements Flow.Processor<String, String> {

        private Flow.Subscription subscription;

        @Override
        public void onSubscribe(Flow.Subscription subscription) {
            this.subscription = subscription;
            this.subscription.request(1);
        }

        @Override
        public void onNext(String item) {
            System.err.println(item + "  from Processor");
            this.submit(item);
            this.subscription.request(1);
        }

        @Override
        public void onError(Throwable throwable) {
            throwable.printStackTrace();
            this.subscription.cancel();
        }

        @Override
        public void onComplete() {
            this.close();
        }
    }
}
