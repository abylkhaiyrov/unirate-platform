package kz.abylkhaiyrov.unirateplatformregistry.security;

import kz.abylkhaiyrov.unirateplatformregistry.entity.Role;
import kz.abylkhaiyrov.unirateplatformregistry.entity.User;
import kz.abylkhaiyrov.unirateplatformregistry.entity.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class PersonDetails implements UserDetails {

    private User user;
    private final UserRole userRole;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Role role = userRole.getRole();
        List<GrantedAuthority> authority = new ArrayList<>();
        authority.add(new SimpleGrantedAuthority("ROLE_" + role.getCode()));
        return authority;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return user.isDeleted() && user.isActive();
    }

    @Override
    public boolean isAccountNonLocked() {
        return user.isDeleted() && user.isActive();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return user.isDeleted() && user.isActive();
    }

    @Override
    public boolean isEnabled() {
        return user.isActive();
    }

}
