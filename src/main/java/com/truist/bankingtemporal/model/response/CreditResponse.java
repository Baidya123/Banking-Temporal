package com.truist.bankingtemporal.model.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class CreditResponse {
    private String message;
    private String errors;
    private ResponseData data;
}
