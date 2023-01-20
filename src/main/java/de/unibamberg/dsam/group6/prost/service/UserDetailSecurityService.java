package de.unibamberg.dsam.group6.prost.service;

import de.unibamberg.dsam.group6.prost.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.*;
import de.unibamberg.dsam.group6.prost.entity.Role;
import de.unibamberg.dsam.group6.prost.entity.Privilege;

@RequiredArgsConstructor
@Service
public class UserDetailSecurityService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = this.userRepository.findUserByUsername(username);

        if (user.isEmpty()) {
            throw new UsernameNotFoundException(String.format("User with username %s does not exit.", username));

        }

//        user.get().setRoles(getAuthorities(user.get().getRoles()));
        return new org.springframework.security.core.userdetails.User(
                user.get().getUsername(), user.get().getPassword(),
                user.get().isEnabled(), true, true,
                true, getAuthorities(user.get().getRoles()));
//        return user.get();
    }

    private Collection<? extends GrantedAuthority> getAuthorities(
            Collection<Role> roles) {

        return getGrantedAuthorities(getPrivileges(roles));
    }

    private List<String> getPrivileges(Collection<Role> roles) {

        List<String> privileges = new ArrayList<>();
        List<Privilege> collection = new ArrayList<>();
        for (Role role : roles) {
            privileges.add(role.getName());
            collection.addAll(role.getPrivileges());
        }
        for (Privilege item : collection) {
            privileges.add(item.getName());
        }
        return privileges;
    }

    private List<GrantedAuthority> getGrantedAuthorities(List<String> privileges) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String privilege : privileges) {
            authorities.add(new SimpleGrantedAuthority(privilege));
        }
        return authorities;
    }
}
