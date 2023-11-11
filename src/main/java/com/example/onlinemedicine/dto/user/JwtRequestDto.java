package com.example.onlinemedicine.dto.user;

import com.example.onlinemedicine.entity.enums.Permissions;
import com.example.onlinemedicine.entity.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class JwtRequestDto {
    private UUID id;
    private UserRole userRole;
    private List<Permissions> permissions;

    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<SimpleGrantedAuthority> simpleGrantedAuthorities = new HashSet<>(Set.of(new SimpleGrantedAuthority("ROLE_" + userRole.name())));
        if (permissions != null) {
            simpleGrantedAuthorities.addAll(permissions.stream().map(
                            permission -> new SimpleGrantedAuthority(permission.name())
                    ).toList()
            );
        }
        return simpleGrantedAuthorities;
    }
}
