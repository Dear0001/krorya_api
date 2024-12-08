package com.kshrd.kroya_api.payload.Bank;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankRequest {

    @NotBlank(message = "account number is require")
    private String accountNumber;

    @NotBlank(message = "account name is require")
    private String accountName;

    private String bankLogo;

    @NotBlank(message = "bank name is require")
    private String bankName;

}
