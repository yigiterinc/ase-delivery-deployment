package com.group5.customerauthenticationservice.repository;

import com.group5.customerauthenticationservice.model.ASEDeliveryUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ASEDeliveryUserRepository extends CrudRepository<ASEDeliveryUser, String> {
    Optional<ASEDeliveryUser> findASEDeliveryUserByEmail(String email);

    List<ASEDeliveryUser> findAll();
}
