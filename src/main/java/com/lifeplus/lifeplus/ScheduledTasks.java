package com.lifeplus.lifeplus;

import com.google.firebase.messaging.*;
import com.lifeplus.lifeplus.model.Patient;
import com.lifeplus.lifeplus.repository.PatientRepository;
import com.lifeplus.lifeplus.repository.RegistrationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class ScheduledTasks {

    private PatientRepository patientRepository;
    private RegistrationTokenRepository registrationTokenRepository;

    @Autowired
    public ScheduledTasks(PatientRepository patientRepository, RegistrationTokenRepository registrationTokenRepository) {
        this.patientRepository = patientRepository;
        this.registrationTokenRepository = registrationTokenRepository;
    }

    @Scheduled(cron="0 0 12 * * *") // Everyday at 12:00 pm
    //@Scheduled(cron="*/10 * * * * *")
    public void notifyWeightUpdate() {

        System.out.println("Notifying weight updates...");

        List<Patient> patients = patientRepository.findAllForWeightNotification(LocalDateTime.now());
        Notification notification = new Notification("Actualización de peso", "Ingrese su peso actual.");

        patients.forEach(patient -> {
            registrationTokenRepository.findById(patient.getUser().getId())
                    .ifPresent(token -> {
                        Message message = Message.builder()
                                .setNotification(notification)
                                .setToken(token.getToken())
                                .build();
                        FirebaseMessaging.getInstance().sendAsync(message);
                    });
        });
    }

}
