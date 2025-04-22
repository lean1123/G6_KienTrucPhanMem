package ktpm17ctt.g6.user.mapper;

import javax.annotation.processing.Generated;
import ktpm17ctt.g6.user.dto.request.UserRequest;
import ktpm17ctt.g6.user.dto.response.UserResponse;
import ktpm17ctt.g6.user.entity.User;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 22.0.2 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public User toUser(UserRequest userRequest) {
        if ( userRequest == null ) {
            return null;
        }

        User.UserBuilder user = User.builder();

        user.accountId( userRequest.getAccountId() );
        user.firstName( userRequest.getFirstName() );
        user.lastName( userRequest.getLastName() );
        user.dob( userRequest.getDob() );
        user.phone( userRequest.getPhone() );
        user.gender( userRequest.getGender() );

        return user.build();
    }

    @Override
    public UserResponse toUserResponse(User user) {
        if ( user == null ) {
            return null;
        }

        UserResponse.UserResponseBuilder userResponse = UserResponse.builder();

        userResponse.id( user.getId() );
        userResponse.firstName( user.getFirstName() );
        userResponse.lastName( user.getLastName() );
        userResponse.dob( user.getDob() );
        userResponse.phone( user.getPhone() );
        userResponse.avatar( user.getAvatar() );
        userResponse.gender( user.getGender() );

        return userResponse.build();
    }
}
