package ktpm17ctt.g6.orderservice.mapper;

import ktpm17ctt.g6.orderservice.dto.response.OrderDetailResponse;
import ktpm17ctt.g6.orderservice.entities.OrderDetail;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderDetailMapper {
    OrderDetailResponse orderDetailToOrderDetailResponse(OrderDetail orderDetail);
}
