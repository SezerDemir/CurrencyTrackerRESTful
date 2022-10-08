package com.sezer.security.controller;

import com.sezer.security.models.Role;
import com.sezer.security.models.User;
import com.sezer.security.service.UserService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;

@RestController
@Data
@Slf4j
@RequestMapping(path = "/api/user")
public class UserController {
    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping(path = "/addUser")
    public ResponseEntity<User> saveUser(@RequestBody User user) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("api/user/addUser").toUriString());
        return ResponseEntity.created(uri).body(userService.saveUser(user));
    }

    @PostMapping(path = "/addRole")
    public ResponseEntity<Role> saveRole(@RequestBody Role role) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("api/user/addRole").toUriString());
        return ResponseEntity.created(uri).body(userService.saveRole(role));
    }

    @PostMapping(path = "/assignRole")
    public ResponseEntity<User> assignRole(@RequestBody RoleToUserBody requestBody) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("api/user/assignRole").toUriString());
        User user = userService.addRoleToUser(requestBody.getUsername(), requestBody.getRoleName());
        return ResponseEntity.created(uri).body(user);
    }

}
@Data
class RoleToUserBody {
    private String username;
    private String roleName;
}