package com.sezer.security.service;

import com.sezer.security.models.Role;
import com.sezer.security.models.User;
import com.sezer.security.repository.RoleRepository;
import com.sezer.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;

@Service @RequiredArgsConstructor @Transactional @Slf4j
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public User saveUser(User user){
        log.info("Saving new user {} to the database", user.getUsername());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public Role saveRole(Role role) {
        log.info("Saving new role {} to the database", role.getName());
        return roleRepository.save(role);
    }

    public User addRoleToUser(String username, String roleName){
        log.info("Adding role {} to user {}", roleName, username);
        User user = userRepository.findByUsername(username).get();
        Role role = roleRepository.findByName(roleName).get();
        user.assignRole(role);
        return userRepository.save(user);
    }

    public User getUser(String username) {
        log.info("Fetching user {} from database", username);
        return userRepository.findByUsername(username).orElse(null);
    }

    public Role getRole(String roleName) {
        return roleRepository.findByName(roleName).orElse(null);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElse(null);
        if(user == null){
            log.error("Username does not exist in database");
            throw new UsernameNotFoundException("Username does not exist in database");
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        });
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }
}
