package ktpm17ctt.g6.user.service;

import ktpm17ctt.g6.user.dto.request.AddressCreationRequest;
import ktpm17ctt.g6.user.dto.response.AddressResponse;
import ktpm17ctt.g6.user.entity.Address;

import java.util.List;

public interface AddressService {
    Address createAddress(AddressCreationRequest request) throws Exception;

    List<AddressResponse> getMyAddress() throws Exception;

    AddressResponse getAddressById(String id) throws Exception;
}
