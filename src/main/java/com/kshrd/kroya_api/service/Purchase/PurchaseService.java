package com.kshrd.kroya_api.service.Purchase;

import com.kshrd.kroya_api.enums.PaymentType;
import com.kshrd.kroya_api.enums.PurchaseStatusType;
import com.kshrd.kroya_api.payload.BaseResponse;
import com.kshrd.kroya_api.payload.Purchase.PurchaseRequest;

public interface PurchaseService {
//    BaseResponse<?> addPurchaseOldCode(PurchaseRequestOldCode purchaseRequestOldCode);

    BaseResponse<?> addPurchase(PurchaseRequest purchaseRequest, PaymentType paymentType);

    BaseResponse<?> getAllOrdersForSeller(Long foodSellId);

    BaseResponse<?> getOrdersForBuyer();

    BaseResponse<?> updateOrderStatus(Long orderId, PurchaseStatusType newStatus);

    BaseResponse<?> getSellerItemsWithOrderCounts();

    BaseResponse<?> getAllOrdersAndSales();

    BaseResponse<?> searchFoodSellInPurchaseByName(String name);
}
