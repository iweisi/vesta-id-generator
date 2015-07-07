package com.wanda.vesta.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class VestaServer {

	public static void main(String[] args) throws IOException {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				"spring/vesta-server-main.xml");

		context.start();

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		while (!"exit".equals(br.readLine()))
			;
	}

}
