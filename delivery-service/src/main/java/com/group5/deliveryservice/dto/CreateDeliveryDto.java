package com.group5.deliveryservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class CreateDeliveryDto {

    private String boxId;
    private String customerId;
    private String delivererId;
}
