package ktpm17ctt.g6.orderservice.mapper;

import ktpm17ctt.g6.orderservice.dto.response.OrderDetailResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderDetailMapper {
    OrderDetailResponse postToPostResponse(OrderDetailResponse orderDetail);
}
