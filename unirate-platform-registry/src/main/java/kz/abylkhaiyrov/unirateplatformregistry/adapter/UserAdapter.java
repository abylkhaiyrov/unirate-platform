package kz.abylkhaiyrov.unirateplatformregistry.adapter;

import kz.abylkhaiyrov.unirateplatformregistry.dto.UserDto;
import kz.abylkhaiyrov.unirateplatformregistry.dto.auth.UserRegisterDto;
import kz.abylkhaiyrov.unirateplatformregistry.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserAdapter {

    User toEntity(UserRegisterDto userRegistrationDto);

    UserDto toDto(User user);
}