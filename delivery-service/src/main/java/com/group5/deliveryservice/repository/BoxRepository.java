package com.group5.deliveryservice.repository;

import com.group5.deliveryservice.model.Box;
import com.group5.deliveryservice.model.Delivery;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoxRepository extends MongoRepository<Box, String> {

    Optional<Box> findByStationName(String name);

    List<Box> findByIdIn(List<String> ids);
}
