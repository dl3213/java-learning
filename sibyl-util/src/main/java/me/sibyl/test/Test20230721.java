package me.sibyl.test;

public class Test20230721 {

    public static void main(String[] args) {
        Demo demo = new Demo();
        demo.test();
    }

    static class Demo {
        private int i = 0;
        private int sum = 0;

        public void test() {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    while (i < 1000) {
                        sum += i;
                        i++;
                    }
                    System.err.println(sum);
                }
            };
            Thread thread1 = new Thread(runnable);
            Thread thread2 = new Thread(runnable);
            thread1.start();
            thread2.start();
        }
    }
}
