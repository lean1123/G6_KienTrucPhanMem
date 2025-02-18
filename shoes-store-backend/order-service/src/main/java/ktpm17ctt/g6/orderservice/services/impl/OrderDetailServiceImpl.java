package ktpm17ctt.g6.orderservice.services.impl;

import ktpm17ctt.g6.orderservice.dto.request.OrderDetailRequest;
import ktpm17ctt.g6.orderservice.dto.response.OrderDetailResponse;
import ktpm17ctt.g6.orderservice.entities.OrderDetail;
import ktpm17ctt.g6.orderservice.repositories.OrderDetailRepository;
import ktpm17ctt.g6.orderservice.services.OrderDetailService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class OrderDetailServiceImpl implements OrderDetailService {
    OrderDetailRepository orderDetailRepository;

    public List<OrderDetail> findAll() {
        return orderDetailRepository.findAll();
    }

    public OrderDetailResponse save(OrderDetailRequest request) {

        return OrderDetailResponse.builder()
                .build();
    }

    public Optional<OrderDetail> findById(String s) {
        return orderDetailRepository.findById(s);
    }

    public void deleteById(String s) {
        orderDetailRepository.deleteById(s);
    }
}
