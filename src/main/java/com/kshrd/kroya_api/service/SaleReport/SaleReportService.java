package com.kshrd.kroya_api.service.SaleReport;

import com.kshrd.kroya_api.payload.BaseResponse;

public interface SaleReportService {

     BaseResponse<?> generateReportByDate(String date);
}
