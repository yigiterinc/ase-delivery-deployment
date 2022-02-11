package com.group5.customerauthenticationservice;

import com.group5.customerauthenticationservice.model.ASEDeliveryUser;
import com.group5.customerauthenticationservice.model.Role;
import com.group5.customerauthenticationservice.repository.ASEDeliveryUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class InitRunner implements CommandLineRunner {

    private final ASEDeliveryUserRepository aseDeliveryUserRepository;
    private final PasswordEncoder passwordEncoder;

    public InitRunner(ASEDeliveryUserRepository aseDeliveryUserRepository, PasswordEncoder passwordEncoder) {
        this.aseDeliveryUserRepository = aseDeliveryUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        aseDeliveryUserRepository.save(new ASEDeliveryUser("yigit.erinc@tum.de", Role.DISPATCHER, passwordEncoder.encode("test123")));
        aseDeliveryUserRepository.save(new ASEDeliveryUser("nils.morbitzer@tum.de", Role.DISPATCHER, passwordEncoder.encode("asedelivery5")));
        aseDeliveryUserRepository.findAll().forEach((user) -> {
            log.info("{}", user);
        });
    }
}
