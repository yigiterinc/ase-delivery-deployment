package com.group5.deliveryservice.controller;

import com.group5.deliveryservice.dto.CreateDeliveryDto;
import com.group5.deliveryservice.model.Delivery;
import com.group5.deliveryservice.service.DeliveryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RestControllerAdvice
@RequestMapping("/deliveries")
public class DeliveryController {

    private final DeliveryService deliveryService;

    public DeliveryController(final DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @GetMapping
    public ResponseEntity<List<Delivery>> getAllDeliveries() {
        return new ResponseEntity<>(deliveryService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{deliveryId}")
    public ResponseEntity<Delivery> getDeliveryById(@PathVariable final String deliveryId) {
        return new ResponseEntity<>(deliveryService.findDeliveryById(deliveryId), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Delivery> updateDelivery(@RequestBody Delivery delivery) {
        return new ResponseEntity<>(deliveryService.updateDelivery(delivery), HttpStatus.OK);
    }

    @GetMapping("/customer/{customerId}/status/active")
    public ResponseEntity<List<Delivery>> getActiveDeliveriesOfCustomer(@PathVariable final String customerId) {
        return new ResponseEntity<>(deliveryService.getActiveDeliveriesOfCustomer(customerId), HttpStatus.OK);
    }

    @GetMapping("/customer/{customerId}/status/delivered")
    public ResponseEntity<List<Delivery>> getPastDeliveriesOfCustomer(@PathVariable final String customerId) {
        return new ResponseEntity<>(deliveryService.getPastDeliveriesOfCustomer(customerId), HttpStatus.OK);
    }

    @GetMapping("/deliverer/{delivererId}")
    public ResponseEntity<List<Delivery>> getAssignedDeliveriesOfDeliverer(@PathVariable String delivererId) {
        return new ResponseEntity<>(deliveryService.getAssignedDeliveriesToDeliverer(delivererId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Delivery> createDelivery(@RequestBody final CreateDeliveryDto createDeliveryDto) {
        return new ResponseEntity<>(deliveryService.createDelivery(createDeliveryDto), HttpStatus.OK);
    }

    @DeleteMapping("/{deliveryId}")
    public void deleteDelivery(@PathVariable String deliveryId) {
        deliveryService.deleteDelivery(deliveryId);
    }

    @PutMapping("/box/{boxId}/collected/deliverer/{delivererId}")
    public List<Delivery> onDeliveriesCollected(@PathVariable final String boxId, @PathVariable final String delivererId) {
        return deliveryService.changeStatusToCollected(boxId, delivererId);
    }

    @PutMapping("/user/{userId}/update/box/{boxId}")
    public List<Delivery> onDeliveryUpdated(@PathVariable final String userId, @PathVariable final String boxId) {
        return deliveryService.changeStatusFromBox(userId, boxId);
    }
}


