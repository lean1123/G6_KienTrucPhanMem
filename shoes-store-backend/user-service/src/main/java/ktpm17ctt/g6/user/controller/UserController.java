package ktpm17ctt.g6.user.controller;


import jakarta.servlet.annotation.MultipartConfig;
import jakarta.validation.Valid;
import ktpm17ctt.g6.user.dto.ApiResponse;
import ktpm17ctt.g6.user.dto.request.AddressCreationRequest;
import ktpm17ctt.g6.user.dto.request.UserRequest;
import ktpm17ctt.g6.user.dto.request.UserUpdationRequest;
import ktpm17ctt.g6.user.dto.response.AddressResponse;
import ktpm17ctt.g6.user.dto.response.UserResponse;
import ktpm17ctt.g6.user.entity.Address;
import ktpm17ctt.g6.user.service.AddressService;
import ktpm17ctt.g6.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping()
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private AddressService addressService;

    @GetMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<List<UserResponse>> findAll() {
        return ApiResponse.<List<UserResponse>>builder()
                .result(userService.findAll())
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<UserResponse> findById(@PathVariable String id) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.findById(id).orElse(null))
                .build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<String> deleteById(@PathVariable String id) {
        userService.deleteById(id);
        return ApiResponse.<String>builder()
                .result("Delete user information successfully")
                .build();
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<List<UserResponse>> search(@RequestParam String keyword) {
        return ApiResponse.<List<UserResponse>>builder()
                .result(userService.search(keyword))
                .build();
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateById(@PathVariable String id, @Valid @ModelAttribute UserUpdationRequest userRequest, BindingResult bindingResult) {
        Map<String, Object> response = new LinkedHashMap<>();

        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new LinkedHashMap<>();
            bindingResult.getFieldErrors().forEach(error -> {
                errors.put(error.getField(), error.getDefaultMessage());
            });
            response.put("status", HttpStatus.BAD_REQUEST.value());
            response.put("errors", errors);
        }

        Optional<UserResponse> user = userService.updateInfo(id, userRequest);
        if (user.isEmpty()) {
            response.put("status", HttpStatus.NOT_FOUND.value());
            response.put("message", "User not found");
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(user);
    }

    @GetMapping("/get-my-profile")
    public ApiResponse<UserResponse> getMyProfile() throws Exception {
        return ApiResponse.<UserResponse>builder()
                .result(this.userService.getMyProfile())
                .build();
    }

    @PostMapping("/create-address")
    public ApiResponse<Address> createAddress(@RequestBody AddressCreationRequest request) throws Exception {
        return ApiResponse.<Address>builder()
                .result(addressService.createAddress(request))
                .build();
    }

    @GetMapping("get-my-address")
    public ApiResponse<List<AddressResponse>> getAddress() throws Exception {
        return ApiResponse.<List<AddressResponse>>builder()
                .result(addressService.getMyAddress())
                .build();
    }
}