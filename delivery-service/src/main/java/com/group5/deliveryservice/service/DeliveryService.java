package com.group5.deliveryservice.service;

import com.group5.deliveryservice.dto.CreateDeliveryDto;
import com.group5.deliveryservice.dto.UserDto;
import com.group5.deliveryservice.exception.*;
import com.group5.deliveryservice.mail.MailService;
import com.group5.deliveryservice.mail.StatusChangeMailRequest;
import com.group5.deliveryservice.model.Box;
import com.group5.deliveryservice.model.Delivery;
import com.group5.deliveryservice.model.DeliveryStatus;
import com.group5.deliveryservice.model.Role;
import com.group5.deliveryservice.repository.DeliveryRepository;
import com.group5.deliveryservice.request.CustomerAuthenticationServiceRequests;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final BoxService boxService;
    private final MailService mailService;

    public DeliveryService(final DeliveryRepository deliveryRepository,
                           final BoxService boxService,
                           final MailService mailService) {
        this.deliveryRepository = deliveryRepository;
        this.boxService = boxService;
        this.mailService = mailService;
    }

    public List<Delivery> getAll() {
        return deliveryRepository.findAll();
    }

    public Delivery findDeliveryById(final String deliveryId) {
        return deliveryRepository.findById(deliveryId).orElseThrow(
                () -> new NotFoundException("Delivery not found for id " + deliveryId));
    }

    public Delivery updateDelivery(Delivery delivery) {
        var userDetails = CustomerAuthenticationServiceRequests.getUserDetails(delivery.getCustomerId());
        var delivererDetails = CustomerAuthenticationServiceRequests.getUserDetails(delivery.getDelivererId());
        var newBox = this.boxService.findById(delivery.getTargetPickupBox().getId());
        delivery.setTargetPickupBox(newBox);

        validateDeliveryDetails(delivery, userDetails, delivererDetails);
        return deliveryRepository.save(delivery);
    }

    public void deleteDelivery(String deliveryId) {
        deliveryRepository.deleteById(deliveryId);
    }

    public List<Delivery> getActiveDeliveriesOfCustomer(final String customerId) {
        return this.deliveryRepository.getDeliveriesByCustomerId(customerId).stream()
                .filter(delivery -> isActiveDelivery.test(delivery))
                .collect(Collectors.toList());
    }

    public List<Delivery> getPastDeliveriesOfCustomer(final String customerId) {
        return this.deliveryRepository.getDeliveriesByCustomerId(customerId)
                .stream().filter(delivery -> !isActiveDelivery.test(delivery))
                .collect(Collectors.toList());
    }

    public List<Delivery> getAssignedDeliveriesToDeliverer(final String delivererId) {
        return this.deliveryRepository.findAllByDelivererId(delivererId);
    }

    public Delivery createDelivery(final CreateDeliveryDto createDeliveryDto) {
        var userDetails = CustomerAuthenticationServiceRequests.getUserDetails(createDeliveryDto.getCustomerId());
        var delivererDetails = CustomerAuthenticationServiceRequests.getUserDetails(createDeliveryDto.getDelivererId());

        validateDeliveryDetails(createDeliveryDto, userDetails, delivererDetails);

        var box = boxService.findById(createDeliveryDto.getBoxId());
        var delivery = deliveryRepository.save(
                new Delivery(createDeliveryDto.getCustomerId(), box, createDeliveryDto.getDelivererId()));

        var userMailAddress = userDetails.getEmail();
        var statusChangeMailRequest = new StatusChangeMailRequest(DeliveryStatus.CREATED, delivery.getId());
        sendMailInNewThread(userMailAddress, statusChangeMailRequest);

        return delivery;
    }

    private void validateDeliveryDetails(CreateDeliveryDto createDeliveryDto, UserDto userDetails, UserDto delivererDetails) {
        // Check that the box does not contain deliveries of any other user
        var boxIsValid = isBoxAvailableForNewDelivery(createDeliveryDto.getCustomerId(), createDeliveryDto.getBoxId());
        if (!boxIsValid) {
            throw new BoxAlreadyFullException();
        }

        // Validate role of user with CAS
        var userId = createDeliveryDto.getCustomerId();
        if (!userHasExpectedRole(userDetails, "CUSTOMER")) {
            throw new InvalidIdException(String.format("The customer with id %s not found!", userId));
        }

        var delivererId = createDeliveryDto.getDelivererId();
        // Validate role of deliverer with CAS
        if (!userHasExpectedRole(delivererDetails, "DELIVERER")) {
            throw new InvalidIdException(String.format("The deliverer with id %s not found!", delivererId));
        }
    }

    private void validateDeliveryDetails(Delivery delivery, UserDto userDetails, UserDto delivererDetails) {
        var createDeliveryDto = new CreateDeliveryDto(delivery.getTargetPickupBox().getId(), delivery.getCustomerId(), delivery.getDelivererId());
        validateDeliveryDetails(createDeliveryDto, userDetails, delivererDetails);
    }

    private boolean userHasExpectedRole(UserDto userDto, String expectedRole) {
        return userDto != null && expectedRole.equals(userDto.getRole());
    }

    Predicate<Delivery> isActiveDelivery = delivery -> !delivery.getDeliveryStatus().equals(DeliveryStatus.DELIVERED);

    public List<Delivery> changeStatusToCollected(final String boxId, String delivererId) {
        final List<String> deliveryIds = deliveryRepository
                .findAllByDeliveryStatusAndDelivererIdAndTargetPickupBoxId(DeliveryStatus.CREATED, delivererId, boxId)
                .stream().map(Delivery::getId).collect(Collectors.toList());

        var deliveriesToSaveUpdates = new ArrayList<Delivery>();
        for (String deliveryId : deliveryIds) {
            Delivery updatedDelivery = null;
            try {
                updatedDelivery = changeSingleDeliveryStatusToCollected(deliveryId, delivererId);
            } catch (InvalidIdException invalidIdException) {
                System.out.println("Delivery or deliverer with id not found - deliveryId:" + deliveryId);
            } catch (InvalidStatusChangeException invalidStatusChangeException) {
                System.out.println("Invalid status change exception in delivery " + deliveryId);
            }

            if (updatedDelivery != null) {
                deliveriesToSaveUpdates.add(updatedDelivery);
            }
        }

        return deliveryRepository.saveAll(deliveriesToSaveUpdates);
    }

    public Delivery changeSingleDeliveryStatusToCollected(final String deliveryId, final String delivererId) {
        var delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(
                        () -> new InvalidIdException(
                                String.format("The delivery with id %s not found!", deliveryId)));

        if (!delivery.getDeliveryStatus().equals(DeliveryStatus.CREATED)) {
            throw new InvalidStatusChangeException();
        }

        var deliverer = CustomerAuthenticationServiceRequests.getUserDetails(delivererId);
        if (!userHasExpectedRole(deliverer, "DELIVERER")) {
            throw new InvalidIdException(String.format("The deliverer with id %s not found!", delivererId));
        }

        if (!delivery.getDelivererId().equals(delivererId)) {
            throw new InvalidIdException("The deliverer id is not equal to delivererId of this delivery");
        }

        delivery.setDeliveryStatus(DeliveryStatus.COLLECTED);
        delivery.setCollectedAt(new Date());

        return delivery;
    }

    public List<Delivery> changeStatusToDeposited(final String delivererId, final String boxId) {

        var deliveries = deliveryRepository.findAllByDeliveryStatusAndDelivererIdAndTargetPickupBoxId(DeliveryStatus.COLLECTED, delivererId, boxId);

        if (deliveries.isEmpty()) {
            throw new NoDeliveriesToDepositException();
        }

        for (var delivery : deliveries) {
            if (!delivery.getDelivererId().equals(delivererId)) {
                throw new InvalidIdException("Supplied delivererId does not match the delivererId of this delivery");
            }

            var deliverer = CustomerAuthenticationServiceRequests.getUserDetails(delivererId);
            if (!userHasExpectedRole(deliverer, "DELIVERER")) {
                throw new InvalidIdException(String.format("The deliverer with id %s not found!", delivererId));
            }

            delivery.setDeliveryStatus(DeliveryStatus.DEPOSITED);
            delivery.setDeliveredAt(new Date());
        }

        var userId = deliveries.get(0).getCustomerId();
        var userDetails = CustomerAuthenticationServiceRequests.getUserDetails(userId);
        var statusChangeMailRequest = new StatusChangeMailRequest(DeliveryStatus.DEPOSITED, deliveries.stream()
                .map(Delivery::getId)
                .collect(Collectors.toList()));

        sendMailInNewThread(userDetails.getEmail(), statusChangeMailRequest);

        return deliveryRepository.saveAll(deliveries);
    }

    private void sendMailInNewThread(String userDetails, StatusChangeMailRequest statusChangeMailRequest) {
        new Thread(() -> mailService.sendEmailTo(userDetails, statusChangeMailRequest)).start();
    }

    public boolean isBoxAvailableForNewDelivery(final String customerId, final String boxId) {
        var box = boxService.findById(boxId);
        return boxOnlyContainsDeliveriesOfThisCustomer(box, customerId);
    }

    private boolean boxOnlyContainsDeliveriesOfThisCustomer(final Box box, final String customerId) {
        assert box != null;

        var deliveriesInBox = deliveryRepository
                .findAllByDeliveryStatusInAndTargetPickupBoxId(List.of(DeliveryStatus.CREATED, DeliveryStatus.DEPOSITED, DeliveryStatus.COLLECTED), box.getId());

        var boxIsEmptyOrHasSingleDelivery = deliveriesInBox.size() <= 1;

        var containsDeliveriesOfThisCustomerOnly = deliveriesInBox
                .stream().allMatch(delivery -> delivery.getCustomerId().equals(customerId));

        return boxIsEmptyOrHasSingleDelivery || containsDeliveriesOfThisCustomerOnly;
    }

    public List<Delivery> changeStatusToDelivered(final String userId, final String boxId) {
        var deliveriesInsideBox = deliveryRepository
                .findAllByTargetPickupBoxIdAndDeliveryStatus(boxId, DeliveryStatus.DEPOSITED);

        if (deliveriesInsideBox.isEmpty()) {
            return new ArrayList<>();   // No delivery inside box
        }

        assert allDeliveriesAreForTheSameUser(deliveriesInsideBox);

        var userDetails = CustomerAuthenticationServiceRequests.getUserDetails(userId);
        var deliveriesToUpdate = new ArrayList<Delivery>();
        for (Delivery delivery : deliveriesInsideBox) {
            if (!delivery.getCustomerId().equals(userId)) {
                throw new InvalidIdException("Deliveries inside the box do not belong to this user!");
            }

            delivery.setDeliveryStatus(DeliveryStatus.DELIVERED);
            deliveriesToUpdate.add(delivery);
            StatusChangeMailRequest statusChangeMailRequest = new StatusChangeMailRequest(DeliveryStatus.DELIVERED);
            sendMailInNewThread(userDetails.getEmail(), statusChangeMailRequest);
        }

        return deliveryRepository.saveAll(deliveriesToUpdate);
    }

    private boolean allDeliveriesAreForTheSameUser(List<Delivery> deliveries) {
        return Collections.frequency(deliveries, deliveries.get(0)) == deliveries.size();
    }

    public List<Delivery> changeStatusFromBox(final String userId, final String boxId) {
        var userRole = CustomerAuthenticationServiceRequests.getUserDetails(userId).getRole();
        if (userRole.equals(Role.DELIVERER.toString()))
            return changeStatusToDeposited(userId, boxId);
        else if (userRole.equals(Role.CUSTOMER.toString()))
            return changeStatusToDelivered(userId, boxId);
        else
            return new LinkedList<>();
    }

}
