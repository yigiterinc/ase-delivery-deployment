package com.group5.deliveryservice.service;

import com.group5.deliveryservice.dto.CreateBoxDto;
import com.group5.deliveryservice.dto.DelivererAssignedBoxDto;
import com.group5.deliveryservice.exception.InvalidIdException;
import com.group5.deliveryservice.model.Box;
import com.group5.deliveryservice.model.Delivery;
import com.group5.deliveryservice.model.DeliveryStatus;
import com.group5.deliveryservice.repository.BoxRepository;
import com.group5.deliveryservice.repository.DeliveryRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BoxService {

    private final BoxRepository boxRepository;
    private final DeliveryRepository deliveryRepository;

    public BoxService(BoxRepository boxRepository, DeliveryRepository deliveryRepository) {
        this.boxRepository = boxRepository;
        this.deliveryRepository = deliveryRepository;
    }

    public Box createBox(final CreateBoxDto boxDto) {
        Box box = new Box(boxDto.getStationName(), boxDto.getStationAddress());
        return boxRepository.save(box);
    }

    public void deleteBox(String boxId) {
        boxRepository.deleteById(boxId);
    }

    public Box findById(String id) {
        return boxRepository.findById(id)
                .orElseThrow(
                        () -> new InvalidIdException(
                                String.format("Box with id %s not found", id)));
    }

    public List<DelivererAssignedBoxDto> getDelivererAssignedBoxes(String delivererId) {
        List<String> boxIds = deliveryRepository.findAllByDelivererId(delivererId)
                .stream()
                .map(Delivery::getTargetPickupBox)
                .distinct()
                .map(Box::getId)
                .collect(Collectors.toList());

        var delivererAssignedBoxDtos = boxIds.stream().map(id -> {
            var box = findById(id);
            var delivery = deliveryRepository.findAllByDeliveryStatusInAndTargetPickupBoxId(
                    List.of(DeliveryStatus.CREATED, DeliveryStatus.COLLECTED), id).get(0);
            var deliveryStatus = delivery.getDeliveryStatus();
            return new DelivererAssignedBoxDto(box, deliveryStatus);
        }).collect(Collectors.toList());

        return delivererAssignedBoxDtos;
    }

    public void checkNameUniqueness(Box box) throws RuntimeException {
        var boxWithSameName = boxRepository.findByStationName(box.getStationName());
        if (boxWithSameName.isPresent() && !box.getId().equals(boxWithSameName.get().getId()))
            throw new RuntimeException("Box with name " + box.getStationName() + " already exists");
    }

    public void checkNameUniqueness(CreateBoxDto createBoxDto) throws RuntimeException {
        var boxWithSameName = boxRepository.findByStationName(createBoxDto.getStationName());
        if (boxWithSameName.isPresent())
            throw new RuntimeException("Box with name " + createBoxDto.getStationName() + " already exists");
    }

    public Box updateBox(String boxId, Box boxDetails) {
        Box box = boxRepository.findById(boxId)
                .orElseThrow(() -> new RuntimeException("Box not found for id " + boxId));
        checkNameUniqueness(boxDetails);
        box.setStationName(boxDetails.getStationName());
        box.setStationAddress(boxDetails.getStationAddress());

        return boxRepository.save(box);
    }

    public List<Box> findAll() {
        return boxRepository.findAll();
    }
}
