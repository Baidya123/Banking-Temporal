package com.truist.bankingtemporal;

import com.truist.bankingtemporal.service.TransactionService;
import com.truist.bankingtemporal.temporal.TemporalClientRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class BankingTemporalApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext appContext = SpringApplication.run(BankingTemporalApplication.class, args);
//        new TemporalClientRunner(appContext.getBean(TransactionService.class)).run();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
