package com.hunch.realestate.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfitInfo {
    private Long monthlyIncome; // 월수입
    private Long monthlyExpense; // 월지출
    private Double profitRate; // 수익률
}