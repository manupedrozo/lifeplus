package com.lifeplus.lifeplus.controller;

import com.lifeplus.lifeplus.model.User;
import com.lifeplus.lifeplus.model.form.NewPasswordForm;
import com.lifeplus.lifeplus.model.form.UserEditForm;
import com.lifeplus.lifeplus.model.UserType;
import com.lifeplus.lifeplus.model.form.PasswordForm;
import com.lifeplus.lifeplus.security.jwt.JWTConfigurer;
import com.lifeplus.lifeplus.security.jwt.JWTToken;
import com.lifeplus.lifeplus.security.jwt.TokenProvider;
import com.lifeplus.lifeplus.service.EmailService;
import com.lifeplus.lifeplus.service.UserService;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final TokenProvider tokenProvider;
    private final EmailService emailService;

    @Autowired
    public UserController(UserService userService, TokenProvider tokenProvider, EmailService emailService) {
        this.userService = userService;
        this.tokenProvider = tokenProvider;
        this.emailService = emailService;
    }

    @GetMapping()
    @Secured("ROLE_ADMIN")
    public List<User> getAllUsers() {return userService.findAll();}

    @Secured("ROLE_ADMIN")
    @GetMapping(value = "/paged")
    public ResponseEntity<Page<User>> getAllUsersPagedAndFiltered(
            @ApiParam(value = "Query param for 'page number'") @Valid @RequestParam(value = "page") int page,
            @ApiParam(value = "Query param for 'page size'") @Valid @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
            @ApiParam(value = "Query param for 'type' filter") @Valid @RequestParam(value = "type", required = false) UserType[] type,
            @ApiParam(value = "Query param for 'name' filter") @Valid @RequestParam(value = "name", required = false, defaultValue = "") String name
            ) {
        if (size == 0) size = 10;
        if (type == null) type = UserType.values();
        Page<User> userPage = userService.findAllPagedAndFiltered(page, size, type, name);
        return ResponseEntity.ok(userPage);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") int id) {
        final Optional<User> optional = userService.findById(id);
        return optional.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping(value = "/by_username/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable("username") String username) {
        final Optional<User> optional = userService.findByUsername(username);
        return optional.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping(value = "/admin")
    @Secured("ROLE_ADMIN")
    public List<User> getAllAdmins() {
        return userService.findAllByType(UserType.ADMIN);
    }

    @GetMapping(value = "/patient")
    public List<User> getAllPatients() {
        return userService.findAllByType(UserType.USER);
    }

    @GetMapping(value = "/medic")
    @Secured({"ROLE_ADMIN", "ROLE_MEDIC", "ROLE_KINESIOLOGIST"})
    public List<User> getAllMedics() {
        return userService.findAllByType(UserType.MEDIC);
    }

    @GetMapping(value = "/kinesiologist")
    public List<User> getAllKinesiologists() {
        return userService.findAllByType(UserType.KINESIOLOGIST);
    }

    @PostMapping()
    @Secured("ROLE_ADMIN")
    public ResponseEntity createUser(@Valid @RequestBody User user, UriComponentsBuilder b) {
        int id = userService.save(user);
        UriComponents components = b.path("/user/{id}").buildAndExpand(id);
        return ResponseEntity.created(components.toUri()).build();
    }

    @PutMapping(value = "/{id}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity modifyUserById(@PathVariable("id") int id, @Valid @RequestBody UserEditForm userEditForm) {
        userService.update(id, userEditForm);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/{id}/change_password")
    @Secured("ROLE_ADMIN")
    public ResponseEntity changePasswordById(@PathVariable("id") int id, @Valid @RequestBody PasswordForm passwordForm) {
        userService.changePasswordById(id, passwordForm);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity deleteUser(@PathVariable int id) {
        return userService.delete(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping(value = "/me")
    public ResponseEntity<User> getCurrentUser() {
        final Optional<User> optional = userService.findCurrent();
        return optional.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping(value = "/me")
    public ResponseEntity modifyCurrentUser(@Valid @RequestBody UserEditForm userEditForm) {
        User user = userService.updateCurrent(userEditForm);

        ArrayList<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getType().toString()));
        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), authorities);
        String jwt = tokenProvider.createToken(authentication);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JWTConfigurer.AUTHORIZATION_HEADER, "Bearer " + jwt);

        return new ResponseEntity<>(new JWTToken(jwt), httpHeaders, HttpStatus.OK);
    }

    @PostMapping(value = "/me/verify_password")
    public ResponseEntity verifyPassword(@Valid @RequestBody PasswordForm passwordForm) {
        boolean result = this.userService.verifyPassword(passwordForm);
        return result ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @PutMapping(value = "/me/change_password")
    public ResponseEntity changePassword(@Valid @RequestBody PasswordForm passwordForm) {
        userService.changeCurrentPassword(passwordForm);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/forgotPassword")
    public ResponseEntity forgotPassword(@RequestBody String email, HttpServletRequest request) {
        Optional<User> userOptional = userService.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setResetToken(UUID.randomUUID().toString());
            userService.justSave(user);
            // TODO: get client host dynamically
            String url = "http://localhost:4200/reset/" + user.getResetToken();
            String body = "Para recuperar tu contraseña debe ingresar al siguiente link: \n" + url;
            emailService.sendSimpleMessage(user.getEmail(), "Recuperar Contraseña", body);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(value = "/resetPassword")
    public ResponseEntity resetPassword(@Valid @RequestBody NewPasswordForm newPasswordForm) {
        Optional<User> userOptional = userService.findByResetToken(newPasswordForm.getToken());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.getEmail().equals(newPasswordForm.getMail())) {
                user.setPassword(newPasswordForm.getPassword());
                user.setResetToken(null);
                userService.hashAndSave(user);
                return ResponseEntity.ok(user.getType());
            }
        }
        return ResponseEntity.notFound().build();
    }
}
