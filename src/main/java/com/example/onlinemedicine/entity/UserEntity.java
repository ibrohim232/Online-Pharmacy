package com.example.onlinemedicine.entity;

import com.example.onlinemedicine.entity.enums.Permissions;
import com.example.onlinemedicine.entity.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@AllArgsConstructor
@Entity
@NoArgsConstructor
@Table(name = "users")
public class UserEntity extends BaseEntity implements UserDetails {
    {
        this.role = UserRole.USER;
    }

    private String fullName;
    @Column(unique = true)
    private String userName;
    @Column(nullable = false)
    private String password;
    @Column(unique = true)
    private String phoneNumber;
    @Enumerated(value = EnumType.STRING)
    private UserRole role;
    @Enumerated(value = EnumType.STRING)
    private List<Permissions> permissions;
    @Column(unique = true)
    private String email;
    @Column(columnDefinition = "boolean default false")
    private boolean isVerify;
    @Column(nullable = false)
    private int code;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<SimpleGrantedAuthority> simpleGrantedAuthorities = new HashSet<>(Set.of(new SimpleGrantedAuthority("ROLE_" + role.name())));
        if (permissions != null) {
            simpleGrantedAuthorities.addAll(permissions.stream().map(
                            permission -> new SimpleGrantedAuthority(permission.name())
                    ).toList()
            );
        }
        return simpleGrantedAuthorities;
    }


    @Override
    public String getPassword() {
        return password;
    }


    @Override
    public String getUsername() {
        return userName;
    }


    @Override
    public boolean isAccountNonExpired() {
        return isActive();
    }

    @Override
    public boolean isAccountNonLocked() {
        return isActive();
    }


    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }


    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserEntity user)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(userName, user.userName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), userName);
    }
}