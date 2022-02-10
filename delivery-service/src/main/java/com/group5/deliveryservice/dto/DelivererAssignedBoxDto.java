package com.group5.deliveryservice.dto;

import com.group5.deliveryservice.model.Box;
import com.group5.deliveryservice.model.Delivery;
import com.group5.deliveryservice.model.DeliveryStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DelivererAssignedBoxDto {
  private Box box;
  private DeliveryStatus statusOfDeliveries;
}
