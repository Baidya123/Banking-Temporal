package com.truist.bankingtemporal.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class BalanceRequest {
    private List<Long> accountNumbers;
}
