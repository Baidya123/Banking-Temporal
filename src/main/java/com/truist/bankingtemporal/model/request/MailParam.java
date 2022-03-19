package com.truist.bankingtemporal.model.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @Builder
public class MailParam {
    private String to;
    private String subject;
    private String message;
}
