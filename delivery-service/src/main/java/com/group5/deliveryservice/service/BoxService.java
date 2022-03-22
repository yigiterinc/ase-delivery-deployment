package com.group5.deliveryservice.service;

import com.group5.deliveryservice.dto.CreateBoxDto;
import com.group5.deliveryservice.dto.DelivererAssignedBoxDto;
import com.group5.deliveryservice.exception.DuplicateBoxNameException;
import com.group5.deliveryservice.exception.NotFoundException;
import com.group5.deliveryservice.model.Box;
import com.group5.deliveryservice.model.Delivery;
import com.group5.deliveryservice.model.DeliveryStatus;
import com.group5.deliveryservice.repository.BoxRepository;
import com.group5.deliveryservice.repository.DeliveryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
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
        checkNameUniqueness(boxDto);
        Box box = new Box(boxDto.getStationName(), boxDto.getStationAddress());
        return boxRepository.save(box);
    }

    public void deleteBox(String boxId) {
        boxRepository.deleteById(boxId);
    }

    public Box findById(String id) {
        return boxRepository.findById(id)
                .orElseThrow(
                        () -> new NotFoundException(
                                String.format("Box with id %s not found", id)));
    }

    public List<DelivererAssignedBoxDto> getDelivererAssignedBoxes(String delivererId) {
        List<String> boxIds = findBoxesByDelivererAndTargetBoxDistinct(delivererId);

        var delivererAssignedBoxDtos = boxIds.stream().map(id -> {
            var box = findById(id);
            var activeDeliveryStatuses = List.of(DeliveryStatus.CREATED, DeliveryStatus.COLLECTED);
            var delivery = deliveryRepository.findAllByDeliveryStatusInAndTargetPickupBoxId(
                    activeDeliveryStatuses, id).get(0);
            var deliveryStatus = delivery.getDeliveryStatus();
            return new DelivererAssignedBoxDto(box, deliveryStatus);
        }).collect(Collectors.toList());

        return delivererAssignedBoxDtos;
    }

    private List<String> findBoxesByDelivererAndTargetBoxDistinct(String delivererId) {
        return deliveryRepository.findAllByDelivererId(delivererId)
                .stream()
                .map(Delivery::getTargetPickupBox)
                .distinct()
                .map(Box::getId)
                .collect(Collectors.toList());
    }

    public void checkNameUniqueness(Box box) throws RuntimeException {
        var boxWithSameName = boxRepository.findByStationName(box.getStationName());
        if (boxWithSameName.isEmpty())  return;

        // This method is called during updates and creates, if updating the box id will be same and operation is allowed
        var isNotUpdating = !box.getId().equals(boxWithSameName.get().getId());
        if (isNotUpdating) {
            throw new DuplicateBoxNameException();
        }
    }

    public void checkNameUniqueness(CreateBoxDto createBoxDto) throws RuntimeException {
        var boxWithSameName = boxRepository.findByStationName(createBoxDto.getStationName());
        if (boxWithSameName.isPresent()) {
            throw new DuplicateBoxNameException();
        }
    }

    public Box updateBox(String boxId, Box boxDetails) {
        Box box = boxRepository.findById(boxId)
                .orElseThrow(() -> new NotFoundException(String.format("Box not found for id %s", boxId)));
        checkNameUniqueness(boxDetails);
        box.setStationName(boxDetails.getStationName());
        box.setStationAddress(boxDetails.getStationAddress());

        return boxRepository.save(box);
    }

    public List<Box> findAll() {
        return boxRepository.findAll();
    }
}
