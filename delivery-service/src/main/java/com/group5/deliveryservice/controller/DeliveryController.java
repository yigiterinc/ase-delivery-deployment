package com.group5.deliveryservice.controller;

import com.group5.deliveryservice.dto.*;
import com.group5.deliveryservice.model.Delivery;
import com.group5.deliveryservice.service.DeliveryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/deliveries")
public class DeliveryController {

    private final DeliveryService deliveryService;

    public DeliveryController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @GetMapping
    public List<Delivery> getAllDeliveries() {
        return deliveryService.getAll();
    }

    @GetMapping("/{deliveryId}")
    public ResponseEntity<Delivery> getDeliveryById(@PathVariable String deliveryId) {
        var delivery = deliveryService.findDeliveryById(deliveryId);
        return ResponseEntity.ok().body(delivery);
    }

    @GetMapping("/customer/{customerId}/status/active")
    public ResponseEntity<List<Delivery>> getActiveDeliveriesOfCustomer(@PathVariable String customerId) {

        List<Delivery> delivery = deliveryService.getActiveDeliveriesOfCustomer(customerId);
        return ResponseEntity.ok().body(delivery);
    }

    @GetMapping("/customer/{customerId}/status/delivered")
    public ResponseEntity<List<Delivery>> getPastDeliveriesOfCustomer(@PathVariable String customerId) {
        List<Delivery> delivery = deliveryService.getPastDeliveriesOfCustomer(customerId);
        return ResponseEntity.ok().body(delivery);
    }

    @GetMapping("/deliverer/{delivererId}")
    public List<Delivery> getAssignedDeliveriesOfDeliverer(@PathVariable String delivererId) {
        return deliveryService.getAssignedDeliveriesToDeliverer(delivererId);
    }

    @PostMapping
    public Delivery createDelivery(@RequestBody final CreateDeliveryDto createDeliveryDto) {
        return deliveryService.createDelivery(createDeliveryDto);
    }

    @DeleteMapping("/{deliveryId}")
    public void deleteDelivery(@PathVariable String deliveryId) {
        deliveryService.deleteDelivery(deliveryId);
    }

    @PutMapping("/box/{boxId}/collected/deliverer/{delivererId}")
    public List<Delivery> onDeliveriesCollected(@PathVariable final String boxId, @PathVariable final String delivererId) {
        return deliveryService.changeStatusToCollected(boxId, delivererId);
    }

    @PutMapping("/deliverer/{delivererId}/deposited/box/{boxId}")
    public List<Delivery> onDeliveryDeposited(@PathVariable final String delivererId, @PathVariable final String boxId) {
        return deliveryService.changeStatusToDeposited(delivererId, boxId);
    }

    @PutMapping("/user/{userId}/delivered/box/{boxId}")
    public List<Delivery> onDeliveryDelivered(@PathVariable final String userId, @PathVariable final String boxId) {
        return deliveryService.changeStatusToDelivered(userId, boxId);
    }
}


