package com.lifeplus.lifeplus.service;

import com.lifeplus.lifeplus.model.User;
import com.lifeplus.lifeplus.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

/**
 * @author Manuel Pedrozo
 */
@Service
public class AuthService implements AuthenticationProvider {

    private UserRepository userRepository;

    @Autowired
    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {

        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        return userRepository.findFirstByUsernameAndActiveIsTrue(username).map(user -> {
            if(BCrypt.checkpw(password, user.getPassword())){
                ArrayList<GrantedAuthority> authorities = new ArrayList<>();
                authorities.add(new SimpleGrantedAuthority(user.getType().toString()));
                return new UsernamePasswordAuthenticationToken(username, password, authorities);
            } else {
                throw new BadCredentialsException("Invalid credentials");
            }
        }).orElseThrow(() -> new BadCredentialsException("Invalid credentials"));
    }

    public boolean authenticate(String username, String password) {
        Optional<User> userOptional = userRepository.findFirstByUsernameAndActiveIsTrue(username);
        if(userOptional.isPresent()) {
            User user = userOptional.get();
            return BCrypt.checkpw(password, user.getPassword());
        }
        return false;
    }

    public boolean checkUsername(String username) {
        return userRepository.findFirstByUsernameAndActiveIsTrue(username).isPresent();
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(
                UsernamePasswordAuthenticationToken.class);
    }
}
