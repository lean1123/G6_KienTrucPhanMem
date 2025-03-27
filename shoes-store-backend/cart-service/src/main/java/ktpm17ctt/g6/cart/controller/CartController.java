package ktpm17ctt.g6.cart.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import ktpm17ctt.g6.cart.dto.request.CartDetailRequest;
import ktpm17ctt.g6.cart.dto.response.ApiResponse;
import ktpm17ctt.g6.cart.dto.response.CartDetailResponse;
import ktpm17ctt.g6.cart.dto.response.ProductItemResponse;
import ktpm17ctt.g6.cart.dto.response.UserResponse;
import ktpm17ctt.g6.cart.enties.CartDetail;
import ktpm17ctt.g6.cart.enties.Cart;
import ktpm17ctt.g6.cart.enties.CartDetailPK;
import ktpm17ctt.g6.cart.feign.ProductFeignClient;
import ktpm17ctt.g6.cart.feign.UserFeignClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import com.fasterxml.jackson.core.type.TypeReference;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


import ktpm17ctt.g6.cart.service.CartDetailService;
import ktpm17ctt.g6.cart.service.CartService;

import java.util.*;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Slf4j
public class CartController {
    @Autowired
    private CartService cartService;

    @Autowired
    private CartDetailService cartDetailService;

    private ProductFeignClient productFeignClient;

    private UserFeignClient userFeignClient;

    private ObjectMapper objectMapper;


    @PostMapping("/addToCart")
    public ResponseEntity<?> addToCart(HttpSession httpSession, @RequestBody CartDetailRequest cartDetailRequest,@RequestParam int size){

        Map<String, Object> response = new LinkedHashMap<>();
        List<CartDetail> cartDetails = getCartFromSession(httpSession);
        //  size=35;

        try {
            handleCarDetail(cartDetails, cartDetailRequest,httpSession,size);
        } catch (IllegalArgumentException e){
            response.put("status", HttpStatus.BAD_REQUEST.value());
            response.put("data", "Not enough stock");
            return ResponseEntity.badRequest().body(response);
        }

        try {
            String cartDetailsJson = objectMapper.writeValueAsString(cartDetails);
            httpSession.setAttribute("cart", cartDetailsJson);
        } catch (Exception e) {
            e.printStackTrace();
        }

//        try {
//            syncCartWithDatabase(cartDetails);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        List<CartDetailResponse> cartDetailResponses = new ArrayList<>();
        for (CartDetail cartDetail : cartDetails) {
            String productItemId= cartDetail.getProductItemId();

            cartDetailResponses.add(CartDetailResponse.builder()
                    .cartDetailPK(cartDetail.getCartDetailPK())
                    .productId(productItemId)
                    .quantity(cartDetail.getQuantity()).build());

        }
        response.put("status",HttpStatus.OK.value());
        response.put("data", cartDetailResponses);
        return ResponseEntity.ok(response);
    }


//    @PutMapping("/updateQuantity")
//    public ResponseEntity<?> updateQuantity(HttpSession session, @RequestBody CartDetailRequest cartDetailRequest) {
//        System.out.println("updateQuantity: " + cartDetailRequest.toString());
//
//        Map<String, Object> response = new LinkedHashMap();
    ////
//        List<CartDetail> cartDetails = getCartFromSession(session);
//
//
//
//        if (cartDetails == null) {
//            response.put("status", HttpStatus.BAD_REQUEST.value());
//            response.put("data", "Cart is empty");
//            return ResponseEntity.badRequest().body(response);
//        }
//
//        ApiResponse<ProductItemResponse> productItemResponseApiResponse= productFeignClient.getProductItemById(cartDetailRequest.getProductItemId());
//
//        if(productItemResponseApiResponse==null){
//            response.put("status", HttpStatus.BAD_REQUEST.value());
//            response.put("data", "Error in update quantity!");
//            return ResponseEntity.badRequest().body(response);
//        }
//
//        ProductItemResponse productItemResponse= productItemResponseApiResponse.getResult();
//
//        for (CartDetail cartDetail : cartDetails) {
//            if (cartDetail.getProductItemId().equals(cartDetailRequest.getProductItemId())) {
//                ApiResponse<Integer> realQTy= productFeignClient.getTotalQuantityByProductItemAndSize(cartDetailRequest.getProductItemId(),cartDetail.getCartDetailPK().getSize());
//                int realQuantity=realQTy.getResult();
//                if (cartDetailRequest.getQuantity() > realQuantity) {
//                    response.put("status", HttpStatus.BAD_REQUEST.value());
//                    response.put("data", "Quantity must be less than current quantity");
//                    return ResponseEntity.badRequest().body(response);
//                }
//                cartDetail.setQuantity(cartDetailRequest.getQuantity());
//                break;
//            }
//        }
//        try{
//            ObjectMapper objectMapper = new ObjectMapper();
//            String cartDetailsJson = objectMapper.writeValueAsString(cartDetails);
//            session.setAttribute("cart", cartDetailsJson);
//        } catch (Exception e) {
//            e.printStackTrace();
//            response.put("status", HttpStatus.BAD_REQUEST.value());
//            response.put("data", "Failed to update quantity");
//            return ResponseEntity.badRequest().body(response);
//        }
//
//        try {
//            syncCartWithDatabase(cartDetails);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        List<CartDetailResponse> cartDetailResponses = new ArrayList<>();
//
//        for (CartDetail cartDetail : cartDetails) {
//            String productItemId = cartDetail.getProductItemId();
//
//            cartDetailResponses.add(CartDetailResponse.builder()
//                    .cartDetailPK(cartDetail.getCartDetailPK())
//                    .productId(productItemId)
//                    .quantity(cartDetail.getQuantity()).build());
//
//        }
//
//        response.put("status", HttpStatus.OK.value());
//        response.put("data", cartDetailResponses);
//        return ResponseEntity.ok(response);
//    }

    @PutMapping("/updateQuantity")
    public ResponseEntity<?> updateQuantity(HttpSession session, @RequestBody CartDetailRequest cartDetailRequest) {
        System.out.println("updateQuantity: " + cartDetailRequest.toString());

        Map<String, Object> response = new LinkedHashMap<>();
        List<CartDetail> cartDetails = getCartFromSession(session);

        if (cartDetails == null || cartDetails.isEmpty()) {
            response.put("status", HttpStatus.BAD_REQUEST.value());
            response.put("data", "Cart is empty");
            return ResponseEntity.badRequest().body(response);
        }

        // Lấy size từ request
        int size = cartDetailRequest.getSize();

        // Gọi API để kiểm tra sản phẩm
        ApiResponse<ProductItemResponse> productItemResponseApiResponse = productFeignClient.getProductItemById(cartDetailRequest.getProductItemId());
        if (productItemResponseApiResponse == null || productItemResponseApiResponse.getResult() == null) {
            response.put("status", HttpStatus.BAD_REQUEST.value());
            response.put("data", "Invalid product item");
            return ResponseEntity.badRequest().body(response);
        }

        // Lấy số lượng tồn kho thực tế
        ApiResponse<Integer> realQtyResponse = productFeignClient.getTotalQuantityByProductItemAndSize(cartDetailRequest.getProductItemId(), size);
        if (realQtyResponse == null || realQtyResponse.getResult() == null) {
            response.put("status", HttpStatus.BAD_REQUEST.value());
            response.put("data", "Failed to retrieve real quantity");
            return ResponseEntity.badRequest().body(response);
        }
        int realQuantity = realQtyResponse.getResult();

        // Cập nhật số lượng trong giỏ hàng
        boolean found = false;
        for (CartDetail cartDetail : cartDetails) {
            if (cartDetail.getCartDetailPK().getProductItemId().equals(cartDetailRequest.getProductItemId()) &&
                    cartDetail.getCartDetailPK().getSize() == size) {

                if (cartDetailRequest.getQuantity() > realQuantity) {
                    response.put("status", HttpStatus.BAD_REQUEST.value());
                    response.put("data", "Quantity must be less than or equal to available stock");
                    return ResponseEntity.badRequest().body(response);
                }
                cartDetail.setQuantity(cartDetailRequest.getQuantity());
                found = true;
                break;
            }
        }

        if (!found) {
            response.put("status", HttpStatus.BAD_REQUEST.value());
            response.put("data", "Product with specified size not found in cart");
            return ResponseEntity.badRequest().body(response);
        }

        // Cập nhật session
        try {
            String cartDetailsJson = objectMapper.writeValueAsString(cartDetails);
            session.setAttribute("cart", cartDetailsJson);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("data", "Failed to update session");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

        // Đồng bộ với database
        try {
            syncCartWithDatabase(cartDetails);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Chuẩn bị response
        List<CartDetailResponse> cartDetailResponses = cartDetails.stream()
                .map(cartDetail -> CartDetailResponse.builder()
                        .cartDetailPK(cartDetail.getCartDetailPK())
                        .productId(cartDetail.getProductItemId())
                        .quantity(cartDetail.getQuantity())
                        .build())
                .toList();

        response.put("status", HttpStatus.OK.value());
        response.put("data", cartDetailResponses);
        return ResponseEntity.ok(response);
    }



//    @GetMapping("/delete")
//    public ResponseEntity<?> deleteCartDetail(@RequestParam String productItemId, HttpSession session) {
//        Map<String, Object> response = new LinkedHashMap<>();
//
//
//        ApiResponse<ProductItemResponse> productItemResponseApiResponse= productFeignClient.getProductItemById(productItemId);
//
//        if(productItemResponseApiResponse==null){
//            response.put("status", HttpStatus.BAD_REQUEST.value());
//            response.put("data", "Product not found");
//            return ResponseEntity.badRequest().body(response);
//        }
//
////	    List<CartDetail> cartDetails = (List<CartDetail>) session.getAttribute("cart");
//
//        String cartDetailsJson = (String) session.getAttribute("cart");
//
//        List<CartDetail> cartDetails = new ArrayList<>();
//
//        if (cartDetailsJson != null && !cartDetailsJson.isEmpty()) {
//            try {
////                ObjectMapper objectMapper = new ObjectMapper();
//                cartDetails = objectMapper.readValue(cartDetailsJson, objectMapper.getTypeFactory().constructCollectionType(List.class, CartDetail.class));
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        if (cartDetails == null || cartDetails.isEmpty()) {
//            response.put("status", HttpStatus.BAD_REQUEST.value());
//            response.put("data", "Cart is empty");
//            return ResponseEntity.badRequest().body(response);
//        }
//
//
//        Iterator<CartDetail> iterator = cartDetails.iterator();
//        while (iterator.hasNext()) {
//            CartDetail cartDetail = iterator.next();
//            if (cartDetail.getProductItemId().equals(productItemId)) {
//                iterator.remove();
//                break;
//            }
//        }
//
//
//        if (!cartDetails.isEmpty()) {
//            session.setAttribute("cart", cartDetails);
//        }
//
////        try {
////            syncCartWithDatabase(cartDetails);
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
//
//
//        List<CartDetailResponse> cartDetailResponses = new ArrayList<>();
//        for (CartDetail cartDetail : cartDetails) {
//
//            String productItemId2 = cartDetail.getProductItemId();
//
//            cartDetailResponses.add(CartDetailResponse.builder()
//                    .cartDetailPK(cartDetail.getCartDetailPK())
//                    .productId(productItemId2)
//                    .quantity(cartDetail.getQuantity()).build());
//        }
//
//        response.put("status", HttpStatus.OK.value());
//        response.put("data", cartDetailResponses);
//        return ResponseEntity.ok(response);
//    }


@DeleteMapping("/delete")
public ResponseEntity<?> deleteCartDetail(@RequestParam String productItemId, @RequestParam int size, HttpSession session) {
    Map<String, Object> response = new LinkedHashMap<>();

    // Lấy giỏ hàng từ session
    List<CartDetail> cartDetails = getCartFromSession(session);

    if (cartDetails.isEmpty()) {
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("data", "Cart is empty");
        return ResponseEntity.badRequest().body(response);
    }

    // Xóa sản phẩm với đúng size khỏi giỏ hàng
    boolean removed = cartDetails.removeIf(cartDetail ->
            cartDetail.getProductItemId().equals(productItemId) &&
                    cartDetail.getCartDetailPK().getSize() == size);

    if (!removed) {
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("data", "Product with specified size not found in cart");
        return ResponseEntity.badRequest().body(response);
    }

    // Cập nhật lại session
    try {
        String cartDetailsJson = objectMapper.writeValueAsString(cartDetails);
        session.setAttribute("cart", cartDetailsJson);
    } catch (Exception e) {
        e.printStackTrace();
        response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.put("data", "Failed to update session");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    // Chuẩn bị response
    List<CartDetailResponse> cartDetailResponses = cartDetails.stream()
            .map(cartDetail -> CartDetailResponse.builder()
                    .cartDetailPK(cartDetail.getCartDetailPK())
                    .productId(cartDetail.getProductItemId())
                    .quantity(cartDetail.getQuantity())
                    .build())
            .toList();

    response.put("status", HttpStatus.OK.value());
    response.put("data", cartDetailResponses);
    return ResponseEntity.ok(response);
}





    @GetMapping
    public ResponseEntity<?> viewCart(HttpSession session) {
        Map<String, Object> response = new LinkedHashMap();

        String cartDetailsJson = (String) session.getAttribute("cart");

        List<CartDetail> cartDetails = new ArrayList<>();

        if (cartDetailsJson != null && !cartDetailsJson.isEmpty()) {
            try {
                System.out.println(cartDetailsJson);
                ObjectMapper objectMapper = new ObjectMapper();
                cartDetails = objectMapper.readValue(cartDetailsJson, objectMapper.getTypeFactory().constructCollectionType(List.class, CartDetail.class));
            } catch (Exception e) {
                e.printStackTrace();
                response.put("status", HttpStatus.BAD_REQUEST.value());
                response.put("data", "Error in view cart");
                return ResponseEntity.badRequest().body(response);
            }
        }

        if (cartDetails == null) {
            response.put("status", HttpStatus.OK.value());
            response.put("data", "Cart is empty");
            return ResponseEntity.ok(response);
        }

//        try {
//            syncCartWithDatabase(cartDetails);
//        } catch (Exception e) {
//            // TODO: handle exception
//            System.out.println("Loi tai day");
//            e.printStackTrace();
//        }

        List<CartDetailResponse> cartDetailResponses = new ArrayList<>();

        for (CartDetail cartDetail : cartDetails) {

            String productItemId = cartDetail.getProductItemId();

            cartDetailResponses.add(CartDetailResponse.builder()
                    .cartDetailPK(cartDetail.getCartDetailPK())
                    .productId(productItemId)
                    .quantity(cartDetail.getQuantity()).build());
        }

        response.put("status", HttpStatus.OK.value());
        response.put("data", cartDetailResponses);
        return ResponseEntity.ok(response);
    }



    //    @SuppressWarnings("unchecked")
//    private List<CartDetail> getCartFromSession(HttpSession httpSession){
//        List<CartDetail> cartDetails = new ArrayList<>();
//        try{
//            String cartDetailsJson = (String) httpSession.getAttribute("cart");
//
//            if(cartDetailsJson != null && !cartDetailsJson.isEmpty()){
//                cartDetails = objectMapper.readValue(cartDetailsJson,objectMapper.getTypeFactory().constructType(List.class, CartDetail.class));
//            }
//        } catch (Exception e){
//            e.printStackTrace();
//        }
//        return cartDetails;
//    }
    @SuppressWarnings("unchecked")
    private List<CartDetail> getCartFromSession(HttpSession httpSession) {
        List<CartDetail> cartDetails = new ArrayList<>();
        try {
            String cartDetailsJson = (String) httpSession.getAttribute("cart");
            if (cartDetailsJson != null && !cartDetailsJson.isEmpty()) {
                cartDetails = objectMapper.readValue(cartDetailsJson, new TypeReference<List<CartDetail>>() {});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cartDetails;
    }


private void handleCarDetail(List<CartDetail> cartDetails, CartDetailRequest cartDetailRequest, HttpSession httpSession, int size) throws IllegalArgumentException {
    ApiResponse<ProductItemResponse> productItemResponseApiResponse = productFeignClient.getProductItemById(cartDetailRequest.getProductItemId());

    if (productItemResponseApiResponse == null || productItemResponseApiResponse.getResult() == null) {
        throw new IllegalArgumentException("ProductItem not found");
    }

    ApiResponse<Integer> realQTy = productFeignClient.getTotalQuantityByProductItemAndSize(cartDetailRequest.getProductItemId(), size);
    if (realQTy == null || realQTy.getResult() == null) {
        throw new IllegalArgumentException("Failed to retrieve real quantity");
    }

    int realQuantity = realQTy.getResult();

    // Kiểm tra sản phẩm cùng productItemId và size đã tồn tại trong giỏ hàng chưa
    for (CartDetail detail : cartDetails) {
        if (detail.getProductItemId().equals(cartDetailRequest.getProductItemId()) &&
                detail.getCartDetailPK().getSize() == size) {

            int newQuantity = detail.getQuantity() + cartDetailRequest.getQuantity();
            if (newQuantity > realQuantity) {
                throw new IllegalArgumentException("Quantity must be less than or equal to available stock");
            }
            detail.setQuantity(newQuantity);
            return; // Không cần tạo mới, chỉ cập nhật số lượng
        }
    }

    // Nếu sản phẩm chưa có trong giỏ hàng, thêm mới vào
    if (cartDetailRequest.getQuantity() > realQuantity) {
        throw new IllegalArgumentException("Quantity must be less than or equal to available stock");
    }

    CartDetail newCartDetail = new CartDetail();
    newCartDetail.setQuantity(cartDetailRequest.getQuantity());
    newCartDetail.setProductItemId(cartDetailRequest.getProductItemId());

    CartDetailPK cartDetailPK = new CartDetailPK();
    cartDetailPK.setProductItemId(cartDetailRequest.getProductItemId());
    cartDetailPK.setSize(size);
    newCartDetail.setCartDetailPK(cartDetailPK);

    cartDetails.add(newCartDetail);
}


    private void syncCartWithDatabase(List<CartDetail> cartDetails) throws IllegalArgumentException{
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        if(email==null)
            return;

        Optional<UserResponse> userOpt = userFeignClient.findByEmail(email);

        if (userOpt.isEmpty())
            return;

        UserResponse user = userOpt.get();
        Cart cart = cartService.findCartByUser(user.getId());
        if(cart == null){
            cart = new Cart();

            String userId= userFeignClient.findById(user.getId()).toString();
            if(userId != null && !userId.isEmpty()) {
                cart.setUserId(userId);
                cartService.save(cart);
            } else {
                throw new IllegalArgumentException("User not found");
            }

            for (CartDetail cartDetail : cartDetails) {
                if (cartDetail.getCart() == null) {
                    CartDetailPK cartDetailPK = new CartDetailPK(cart.getId(), cartDetail.getProductItemId(), cartDetail.getCartDetailPK().getSize());
                    cartDetail.setCartDetailPK(cartDetailPK);
                    cartDetail.setCart(cart);
                    cartDetailService.save(cartDetail);
                }
            }
        }}




    @GetMapping("/hehe")
    public String hienThi(){
        System.out.println("hehe");
        return "hehe";
    }
}