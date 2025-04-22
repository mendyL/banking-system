package com.bank.payment.it;

import com.bank.payment.BankingQueueServiceApplication;
import org.springframework.boot.SpringApplication;
import org.testcontainers.utility.TestcontainersConfiguration;

public class TestBankingQueueServiceApplication {

	public static void main(String[] args) {
		SpringApplication.from(BankingQueueServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
