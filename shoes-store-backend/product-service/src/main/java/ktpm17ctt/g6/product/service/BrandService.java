package ktpm17ctt.g6.product.service;

import ktpm17ctt.g6.product.dto.request.BrandRequest;
import ktpm17ctt.g6.product.dto.response.BrandResponse;

import java.util.List;
import java.util.Optional;

public interface BrandService {
    Optional<BrandResponse> findById(String id);
    Optional<BrandResponse> findByName(String name);
    BrandResponse save(BrandRequest brandRequest);
    BrandResponse update(String id, BrandRequest brandRequest);
    void deleteById(String id);
    List<BrandResponse> findAll();
    List<BrandResponse> search(String keyword);
}
