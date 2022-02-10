package com.group5.deliveryservice.repository;

import com.group5.deliveryservice.model.Delivery;
import com.group5.deliveryservice.model.DeliveryStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeliveryRepository extends MongoRepository<Delivery, String> {

    List<Delivery> getDeliveriesByCustomerId(String customerId);

    void deleteById(String deliveryId);

    List<Delivery> findAllByDeliveryStatusAndDelivererId(DeliveryStatus created, String delivererId);

    List<Delivery> findAllByDelivererId(String delivererId);

    List<Delivery> findAllByDeliveryStatusInAndTargetPickupBoxId(List<DeliveryStatus> deliveryStatuses, String boxId);

    List<Delivery> findAllByDeliveryStatusAndDelivererIdAndTargetPickupBoxId(DeliveryStatus deliveryStatus, String delivererId, String boxId);

    List<Delivery> findAllByTargetPickupBoxIdAndDeliveryStatus(String boxId, DeliveryStatus deliveryStatus);
}
