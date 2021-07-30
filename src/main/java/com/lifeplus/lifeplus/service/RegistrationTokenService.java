package com.lifeplus.lifeplus.service;

import com.lifeplus.lifeplus.model.Dto.RegistrationTokenDto;
import com.lifeplus.lifeplus.model.RegistrationToken;
import com.lifeplus.lifeplus.model.User;
import com.lifeplus.lifeplus.repository.RegistrationTokenRepository;
import com.lifeplus.lifeplus.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RegistrationTokenService {

    private RegistrationTokenRepository registrationTokenRepository;
    private UserRepository userRepository;

    @Autowired
    public RegistrationTokenService(
            UserRepository userRepository,
            RegistrationTokenRepository registrationTokenRepository) {
        this.registrationTokenRepository = registrationTokenRepository;
        this.userRepository = userRepository;
    }

    public void registerDeviceToken(RegistrationTokenDto registrationToken) {
        String username = ((org.springframework.security.core.userdetails.User)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();

        Optional<User> userOptional = userRepository.findFirstByUsernameAndActiveIsTrue(username);

        userOptional.ifPresent(user -> {
            RegistrationToken token = new RegistrationToken();
            token.setId(user.getId());
            token.setToken(registrationToken.getToken());
            registrationTokenRepository.save(token);
        });

    }
}
