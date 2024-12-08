package com.kshrd.kroya_api.controller;

import com.kshrd.kroya_api.enums.PaymentType;
import com.kshrd.kroya_api.enums.PurchaseStatusType;
import com.kshrd.kroya_api.payload.BaseResponse;
import com.kshrd.kroya_api.payload.Purchase.PurchaseRequest;
import com.kshrd.kroya_api.service.FoodSell.FoodSellService;
import com.kshrd.kroya_api.service.Purchase.PurchaseService;
import com.kshrd.kroya_api.service.Receipt.ReceiptService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/purchase")
@AllArgsConstructor
@Slf4j
public class PurchaseController {

    private final PurchaseService purchaseService;
    private final ReceiptService receiptService;
    private final FoodSellService foodSellService;

    @Operation(
            summary = "➕ Add a Purchase",
            description = """
                    Creates a new purchase record.
                    **📩 Request Body**:
                    - **purchaseRequest**: JSON object containing purchase details.
                    - **paymentType**: Type of payment (e.g., CREDIT_CARD, CASH).
                    
                    **📩 Response Summary**:
                    - **200**: ✅ Purchase created successfully.
                    - **400**: 🚫 Invalid data provided.
                    """
    )
    @PostMapping
    public BaseResponse<?> addPurchase(@RequestBody PurchaseRequest purchaseRequest, @RequestParam PaymentType paymentType) {
        return purchaseService.addPurchase(purchaseRequest, paymentType);
    }

    @Operation(
            summary = "📄 Get Orders for Seller by FoodSell ID",
            description = """
                    Fetches all orders related to a particular food sell for the seller.
                    - **Path Variable**: **foodSellId**: ID of the food sell item.
                  
                    **📩 Response Summary**:
                    - **200**: ✅ Orders fetched successfully.
                    - **404**: 🚫 No orders found for the specified food sell ID.
                    """
    )
    @GetMapping("/orders/seller/{foodSellId}")
    public BaseResponse<?> getAllOrdersForSeller(@PathVariable Long foodSellId) {
        return purchaseService.getAllOrdersForSeller(foodSellId);
    }

    @Operation(
            summary = "🛍 Get All Orders for Buyer",
            description = """
                    Fetches all purchase orders for the current authenticated buyer.
                    
                    **📩 Response Summary**:
                    - **200**: ✅ Orders fetched successfully.
                    - **404**: 🚫 No orders found for the buyer.
                    """
    )
    @GetMapping("/orders/buyer")
    public BaseResponse<?> getBuyerOrders() {
        return purchaseService.getOrdersForBuyer();
    }

    @Operation(
            summary = "🔄 Update Order Status",
            description = """
                    Updates the status of a specific purchase order.
                    - **Path Variable**: **purchaseId**: ID of the purchase order.
                    - **Request Parameter**: **newStatus**: New status of the purchase order (e.g., COMPLETED, CANCELLED).
                    
                    **📩 Response Summary**:
                    - **200**: ✅ Order status updated successfully.
                    - **404**: 🚫 Purchase order not found.
                    """
    )
    @PutMapping("/orders/{purchaseId}/status")
    public BaseResponse<?> updateOrderStatus(
            @PathVariable Long purchaseId,
            @RequestParam PurchaseStatusType newStatus) {
        return purchaseService.updateOrderStatus(purchaseId, newStatus);
    }

    @Operation(
            summary = "🍱 Get Seller Items with Order Counts",
            description = """
                    Retrieves the seller's food items with the count of orders for each item.
                    
                    **📩 Response Summary**:
                    - **200**: ✅ Seller items with order counts fetched successfully.
                    """
    )
    @GetMapping("/seller/items")
    public BaseResponse<?> getSellerItemsWithOrderCounts() {
        return purchaseService.getSellerItemsWithOrderCounts();
    }

    @Operation(
            summary = "📦 Get All Orders and Sales",
            description = """
                    Fetches all purchase orders and sales records.
                    
                    **📩 Response Summary**:
                    - **200**: ✅ Orders and sales records retrieved successfully.
                    """
    )
    @GetMapping("/all")
    public BaseResponse<?> getAllOrdersAndSales() {
        return purchaseService.getAllOrdersAndSales();
    }

    @Operation(
            summary = "🧾 Get Receipt by Purchase ID",
            description = """
                    Retrieves the receipt for a specific purchase.
                    - **Path Variable**: **purchaseId**: ID of the purchase order.
                    
                    **📩 Response Summary**:
                    - **200**: ✅ Receipt fetched successfully.
                    - **404**: 🚫 Receipt not found for the specified purchase ID.
                    """
    )
    @GetMapping("/receipt/{purchaseId}")
    public BaseResponse<?> getReceiptByPurchaseId(@PathVariable Long purchaseId) {
        return receiptService.getReceiptByPurchaseId(purchaseId);
    }

    @Operation(
            summary = "🔍 Search Food Sell in Purchases by Name",
            description = """
                    Searches for food sell items in purchase records by name.
                    - **Request Parameter**: **name**: Name of the food sell item to search.
                    
                    **📩 Response Summary**:
                    - **200**: ✅ Search results retrieved successfully.
                    - **404**: 🚫 No matching food sell items found in purchases.
                    """
    )
    @GetMapping("/search")
    public BaseResponse<?> searchFoodSellInPurchaseByName(@RequestParam String name) {
        return purchaseService.searchFoodSellInPurchaseByName(name);
    }
}
