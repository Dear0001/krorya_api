package com.kshrd.kroya_api.controller;

import com.kshrd.kroya_api.payload.BaseResponse;
import com.kshrd.kroya_api.service.SaleReport.SaleReportService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/sale-report")
@RequiredArgsConstructor
public class SaleReportController {

    private final SaleReportService saleReportService;


    @Operation(
            summary = "📊 Generate Sales Report by Date",
            description = """
        Retrieve a detailed sales report for a specific date, including all accepted orders and their total sales amount.
        
        **Parameters:**
        - **selectDate**: The date for which to generate the report, formatted as `yyyy-MM-dd`.

        **Response Summary:**
        - **200**: Sales report generated successfully, including daily and monthly totals.
        - **400**: Bad request if the date format is invalid.
        """
    )
    @GetMapping("/{selectDate}")
    public BaseResponse<?> saleReport(@PathVariable String selectDate) {
        return saleReportService.generateReportByDate(selectDate);
    }
}
