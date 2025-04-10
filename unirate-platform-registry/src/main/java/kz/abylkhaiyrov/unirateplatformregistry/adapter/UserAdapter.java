package kz.abylkhaiyrov.unirateplatformregistry.adapter;

import kz.abylkhaiyrov.unirateplatformregistry.dto.UserDto;
import kz.abylkhaiyrov.unirateplatformregistry.dto.auth.UserRegisterDto;
import kz.abylkhaiyrov.unirateplatformregistry.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserAdapter {

    User toEntity(UserRegisterDto userRegistrationDto);

    @Mapping(target = "userProfileImageUrl" , source = "profileImageUrl")
    UserDto toDto(User user);
}