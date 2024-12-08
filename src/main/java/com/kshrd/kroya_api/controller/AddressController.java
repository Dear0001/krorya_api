package com.kshrd.kroya_api.controller;

import com.kshrd.kroya_api.payload.Address.AddressRequest;
import com.kshrd.kroya_api.payload.BaseResponse;
import com.kshrd.kroya_api.service.Address.AddressService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/address")
@AllArgsConstructor
@Slf4j
public class AddressController {

    private final AddressService addressService;

    @Operation(
            summary = "🏠 Create a New Address",
            description = " Adds a new address to the user's address list."
    )
    @PostMapping("/create")
    public BaseResponse<?> createAddress(@RequestBody @Valid AddressRequest addressRequest) {
        return addressService.createAddress(addressRequest);
    }

    @Operation(
            summary = "📋 Get All Addresses",
            description = "Retrieves a list of all addresses for the current user."
    )
    @GetMapping("/list")
    public BaseResponse<?> getAllAddresses() {
        return addressService.getAllAddresses();
    }

    @Operation(
            summary = "🔍 Get Address by ID",
            description = "Fetches a specific address by its ID."
    )
    @GetMapping("/{id}")
    public BaseResponse<?> getAddressById(@PathVariable Long id) {
        return addressService.getAddressById(id);
    }

    @Operation(
            summary = "✏️ Update an Existing Address",
            description = "Updates an existing address with new information."
    )
    @PutMapping("/update/{id}")
    public BaseResponse<?> updateAddress(@PathVariable Long id, @RequestBody @Valid AddressRequest addressRequest) {
        return addressService.updateAddress(id, addressRequest);
    }

    @Operation(
            summary = "🗑️ Delete an Address",
            description = "Deletes an address from the user's address list by its ID."
    )
    @DeleteMapping("/delete/{id}")
    public BaseResponse<?> deleteAddress(@PathVariable Long id) {
        return addressService.deleteAddress(id);
    }
}

