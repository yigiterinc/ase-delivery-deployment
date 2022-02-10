package com.group5.deliveryservice.controller;

import com.group5.deliveryservice.dto.CreateBoxDto;
import com.group5.deliveryservice.dto.DelivererAssignedBoxDto;
import com.group5.deliveryservice.model.Box;
import com.group5.deliveryservice.model.Delivery;
import com.group5.deliveryservice.model.DeliveryStatus;
import com.group5.deliveryservice.repository.BoxRepository;
import com.group5.deliveryservice.repository.DeliveryRepository;
import com.group5.deliveryservice.service.BoxService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/boxes")
public class BoxController {

    private final BoxService boxService;

    public BoxController(BoxService boxService) {
        this.boxService = boxService;
    }

    @GetMapping("/all")
    public List<Box> getAllBoxes() {
        return boxService.findAll();
    }

    @GetMapping("/{id}")
    public Box getBoxById(@PathVariable(value = "id") String boxId) {
        return boxService.findById(boxId);
    }

    @GetMapping("/deliverer/{delivererId}")
    public List<DelivererAssignedBoxDto> getBoxByDelivererId(@PathVariable String delivererId) {
        return boxService.getDelivererAssignedBoxes(delivererId);
    }

    @PostMapping
    public Box createBox(@RequestBody CreateBoxDto boxDto) {
        return boxService.createBox(boxDto);
    }

    @PutMapping("/{id}")
    public Box updateBox(@PathVariable(value = "id") String boxId,
                                         @Valid @RequestBody Box box) {
        return boxService.updateBox(boxId, box);
    }

    @DeleteMapping("/{id}")
    public void deleteBox(@PathVariable(value = "id") String boxId) {
        boxService.deleteBox(boxId);
    }
}
