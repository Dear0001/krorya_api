package com.kshrd.kroya_api.service.Address;

import com.kshrd.kroya_api.payload.Address.AddressRequest;
import com.kshrd.kroya_api.payload.BaseResponse;

public interface AddressService {
    BaseResponse<?> createAddress(AddressRequest addressRequest);

    BaseResponse<?> getAllAddresses();

    BaseResponse<?> getAddressById(Long id);

    BaseResponse<?> updateAddress(Long id, AddressRequest addressRequest);

    BaseResponse<?> deleteAddress(Long id);
}
