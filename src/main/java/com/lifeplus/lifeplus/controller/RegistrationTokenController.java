package com.lifeplus.lifeplus.controller;

import com.lifeplus.lifeplus.model.Dto.RegistrationTokenDto;
import com.lifeplus.lifeplus.service.RegistrationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/token")
public class RegistrationTokenController {

    private RegistrationTokenService registrationTokenService;

    @Autowired
    public RegistrationTokenController(RegistrationTokenService registrationTokenService) {
        this.registrationTokenService = registrationTokenService;
    }

    @PostMapping
    public ResponseEntity registerDeviceToken(@Valid @RequestBody RegistrationTokenDto registrationToken) {
        registrationTokenService.registerDeviceToken(registrationToken);
        return ResponseEntity.ok("");
    }
}
