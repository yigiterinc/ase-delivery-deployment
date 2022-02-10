package com.group5.deliveryservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class CreateDeliveryDto {
    private String boxId;
    private String customerId;
    private String delivererId;
}
