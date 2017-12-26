package com.robert.vesta.client;

import com.robert.vesta.service.intf.IdService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class PerfTest {

    public static void main(String[] args) throws InterruptedException {
        ApplicationContext ac = new ClassPathXmlApplicationContext(
                "spring/vesta-client-test.xml");

        final IdService idService = (IdService) ac.getBean("idService");

        final long[][] times = new long[100][100];

        Thread[] threads = new Thread[100];
        for (int i = 0; i < threads.length; i++) {
            final int ip = i;
            threads[i] = new Thread() {
                @Override
                public void run() {
                    for (int j = 0; j < 100; j++) {
                        long t1 = System.nanoTime();

                        idService.genId();

                        long t = System.nanoTime() - t1;

                        times[ip][j] = t;
                    }
                }

            };
        }

        long lastMilis = System.currentTimeMillis();

        for (int i = 0; i < threads.length; i++) {
            threads[i].start();
        }

        for (int i = 0; i < threads.length; i++) {
            threads[i].join();
        }

        System.out
                .println("QPS: "
                        + ((10000 * 1000 / (System.currentTimeMillis() - lastMilis))));

        long sum = 0;
        long max = 0;
        for (int i = 0; i < times.length; i++) {
            for (int j = 0; j < times[i].length; j++) {
                sum += times[i][j];

                if (times[i][j] > max)
                    max = times[i][j];
            }
        }

        System.out.println("AVG(ms): " + sum / 1000000 / 10000);
        System.out.println("MAX(ms): " + max / 1000000);
    }

}
