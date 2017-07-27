package io.thingcare.bootstrap.be.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class SecurityUserDetails extends User implements UserDetails {
    public SecurityUserDetails(User user) {
        if (user == null) return;
        this.setId(user.getId());
        this.setEmail(user.getEmail());
        this.setFirstName(user.getFirstName());
        this.setLastName(user.getLastName());
        this.setPassword(user.getPassword());
        this.setCreatedDate(user.getCreatedDate());
        this.setLastModifiedDate(user.getLastModifiedDate());
        this.setRoles(user.getRoles());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        for (Role role : super.getRoles()) {
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.getType().name());
            authorities.add(authority);
        }
        return authorities;
    }

    @Override
    public String getUsername() {
        return super.getEmail();
    }

    @Override
    public String getPassword() {
        return super.getPassword();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
