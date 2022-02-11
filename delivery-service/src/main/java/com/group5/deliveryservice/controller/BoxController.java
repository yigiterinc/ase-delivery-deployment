package com.group5.deliveryservice.controller;

import com.group5.deliveryservice.dto.CreateBoxDto;
import com.group5.deliveryservice.dto.DelivererAssignedBoxDto;
import com.group5.deliveryservice.model.Box;
import com.group5.deliveryservice.service.BoxService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.ws.rs.core.Response;
import java.util.List;

@RestController
@RequestMapping("/boxes")
public class BoxController {

    private final BoxService boxService;

    public BoxController(final BoxService boxService) {
        this.boxService = boxService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Box>> getAllBoxes() {
        return new ResponseEntity<>(boxService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Box> getBoxById(@PathVariable(value = "id") final String boxId) {
        return new ResponseEntity<>(boxService.findById(boxId), HttpStatus.OK);
    }

    @GetMapping("/deliverer/{delivererId}")
    public ResponseEntity<List<DelivererAssignedBoxDto>> getBoxByDelivererId(@PathVariable final String delivererId) {
        return new ResponseEntity<>(boxService.getDelivererAssignedBoxes(delivererId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Box> createBox(@RequestBody final CreateBoxDto boxDto) {
        return new ResponseEntity<>(boxService.createBox(boxDto), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Box> updateBox(@PathVariable(value = "id") final String boxId,
                         @Valid @RequestBody final Box box) {

        return new ResponseEntity<>(boxService.updateBox(boxId, box), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBox(@PathVariable(value = "id") final String boxId) {
        boxService.deleteBox(boxId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
