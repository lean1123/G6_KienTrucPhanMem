package ktpm17ctt.g6.user.service.impl;

import ktpm17ctt.g6.user.dto.request.AddressCreationRequest;
import ktpm17ctt.g6.user.dto.response.UserResponse;
import ktpm17ctt.g6.user.entity.Address;
import ktpm17ctt.g6.user.entity.User;
import ktpm17ctt.g6.user.mapper.UserMapper;
import ktpm17ctt.g6.user.repository.AddressRepository;
import ktpm17ctt.g6.user.service.AddressService;
import ktpm17ctt.g6.user.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AddressServiceImpl implements AddressService {
    AddressRepository addressRepository;
    UserService userService;
    UserMapper userMapper;


    @Override
    public Address createAddress(AddressCreationRequest request) throws Exception {
        // Tìm user theo userId trong request
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = null;
        if(authentication != null && authentication.isAuthenticated()){
            email = authentication.getName();
            log.info("Email Logged: {}", email);
        }

        UserResponse userResponse = userService.getUserFromEmail(email);

        if (userResponse == null) {
            throw new Exception("User not found");
        }

        User user = User.builder()
                .id(userResponse.getId())
                .dob(userResponse.getDob())
                .build();

        // Tạo Address entity từ request
        Address address = Address.builder()
                .homeNumber(request.getHomeNumber())
                .ward(request.getWard())
                .district(request.getDistrict())
                .city(request.getCity())
                .user(user)
                .build();

        // Lưu vào DB
        return addressRepository.save(address);
    }

    @Override
    public List<Address> getMyAddress() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = null;
        if(authentication != null && authentication.isAuthenticated()){
            email = authentication.getName();
            log.info("Email Logged: {}", email);
        }

        UserResponse userResponse = userService.getUserFromEmail(email);

        if (userResponse == null) {
            throw new Exception("User not found");
        }
        return addressRepository.findByUserId(userResponse.getId());
    }

}
