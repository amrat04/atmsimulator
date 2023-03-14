package com.amrat.atmsimulator;

import com.amrat.atmsimulator.controller.ATMController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AtmsimulatorApplication implements CommandLineRunner {


	@Autowired
	ATMController atmController;

	public static void main(String[] args) {
		SpringApplication.run(AtmsimulatorApplication.class, args);
	}

	@Override
	public void run(String... args) {
		atmController.doProcess(System.in, System.out);
	}

}
