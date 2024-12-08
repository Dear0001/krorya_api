package com.kshrd.kroya_api.service.Receipt;

import com.kshrd.kroya_api.payload.BaseResponse;
import com.kshrd.kroya_api.payload.Receipt.ReceiptResponse;
import org.springframework.stereotype.Service;

@Service
public interface ReceiptService {
    BaseResponse<?> getReceiptByPurchaseId(Long purchaseId);
}
