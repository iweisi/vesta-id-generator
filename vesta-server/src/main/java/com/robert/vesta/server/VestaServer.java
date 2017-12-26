package com.robert.vesta.server;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class VestaServer {

    public static void main(String[] args) throws IOException {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
                "spring/vesta-server-main.xml");

        context.start();

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        while (!"exit".equals(br.readLine()))
            try {
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                // If error, print it to console
                e.printStackTrace();
            }
        ;
    }

}
