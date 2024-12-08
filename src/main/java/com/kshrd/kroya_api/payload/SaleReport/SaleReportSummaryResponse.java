package com.kshrd.kroya_api.payload.SaleReport;

import com.kshrd.kroya_api.payload.Purchase.PurchaseResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SaleReportSummaryResponse {
    private double totalSales;
    private int totalOrders;
    private List<PurchaseResponse> purchaseResponses;
}

