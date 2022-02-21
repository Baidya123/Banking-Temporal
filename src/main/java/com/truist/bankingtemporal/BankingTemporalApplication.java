package com.truist.bankingtemporal;

import com.truist.bankingtemporal.service.TransactionService;
import com.truist.bankingtemporal.temporal.TemporalClientRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class BankingTemporalApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext appContext = SpringApplication.run(BankingTemporalApplication.class, args);
        new TemporalClientRunner().run();
    }

}
