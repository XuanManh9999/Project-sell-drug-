package com.back_end.myProject.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RevenueStatisticsDTO {
    private Long totalBills;
    private Double totalRevenue;
    private Long totalProductsSold;
    private Long totalAccounts;
}
