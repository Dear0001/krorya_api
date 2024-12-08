package com.kshrd.kroya_api.payload.Purchase;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseRequestOldCode {
    private Long addressDeliveryId;
    private Long foodSellId;
    private Boolean paymentMethod;
    private String remark;
    private String reference;
    private String paidBy;
}
