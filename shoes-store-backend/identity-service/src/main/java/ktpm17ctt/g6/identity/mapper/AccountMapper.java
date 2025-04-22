package ktpm17ctt.g6.identity.mapper;

import ktpm17ctt.g6.identity.dto.request.LoginSocialRequest;
import ktpm17ctt.g6.identity.dto.request.RegistrationRequest;
import ktpm17ctt.g6.identity.dto.request.AccountUpdateRequest;
import ktpm17ctt.g6.identity.dto.response.AccountResponse;
import ktpm17ctt.g6.identity.entity.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    Account toAccount(RegistrationRequest request);

    AccountResponse toAccountResponse(Account account);

    @Mapping(target = "roles", ignore = true)
    void updateAccount(@MappingTarget Account account, AccountUpdateRequest request);
}
