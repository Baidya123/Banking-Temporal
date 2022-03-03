package com.truist.bankingtemporal.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("truist.service-url")
@Getter @Setter
public class ServiceConfig {
    private String debit;
    private String credit;
    private String notify;
    private String balance;
    private String debitRollback;
    private String creditRollback;
}
