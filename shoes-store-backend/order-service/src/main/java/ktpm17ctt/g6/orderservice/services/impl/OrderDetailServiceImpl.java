package ktpm17ctt.g6.orderservice.services.impl;

import ktpm17ctt.g6.orderservice.dto.feinClient.product.ProductItemRequest;
import ktpm17ctt.g6.orderservice.dto.feinClient.product.ProductItemResponse;
import ktpm17ctt.g6.orderservice.dto.request.OrderDetailRequest;
import ktpm17ctt.g6.orderservice.dto.response.OrderDetailResponse;
import ktpm17ctt.g6.orderservice.entities.Order;
import ktpm17ctt.g6.orderservice.entities.OrderDetail;
import ktpm17ctt.g6.orderservice.mapper.OrderDetailMapper;
import ktpm17ctt.g6.orderservice.repositories.OrderDetailRepository;
import ktpm17ctt.g6.orderservice.repositories.httpClients.ProductItemClient;
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
    ProductItemClient productItemClient;
    OrderDetailMapper orderDetailMapper;

    public List<OrderDetail> findAll() {
        return orderDetailRepository.findAll();
    }

    @Override
    public OrderDetailResponse save(OrderDetailRequest request, Order order) throws Exception {

        ProductItemResponse productItemResponse = productItemClient.getProductItem(request.getProductItemId())
                .getResult();

        if(productItemResponse == null) {
            throw new Exception("Product item not found");
        }

        OrderDetail orderDetailResponse = orderDetailRepository.save(OrderDetail.builder()
                .quantity(request.getQuantity())
                .price(productItemResponse.getPrice())
                .productItemId(productItemResponse.getId())
                .size(request.getSize())
                .order(order)
                .build());

//        create a new ProductItemRequest to change the quantity of the product item
        ProductItemRequest productItemRequest =
                ProductItemRequest.builder()

                        .build();

        return orderDetailMapper.orderDetailToOrderDetailResponse(orderDetailResponse);
    }

    public Optional<OrderDetail> findById(String s) {
        return orderDetailRepository.findById(s);
    }

    public void deleteById(String s) {
        orderDetailRepository.deleteById(s);
    }

    @Override
    public List<OrderDetailResponse> findOrderDetailByOrder_Id(String orderId) {
        return orderDetailRepository.findOrderDetailByOrder_Id(orderId)
                .stream()
                .map(orderDetail -> OrderDetailResponse.builder()
                        .productItem(productItemClient.getProductItem(orderDetail.getProductItemId()).getResult())
                        .size(orderDetail.getSize())
                        .quantity(orderDetail.getQuantity())
                        .id(orderDetail.getId())
                        .price(orderDetail.getPrice())
                        .build()).toList();
    }
}
