package com.group5.deliveryservice.request;

import com.group5.deliveryservice.DeliveryServiceApplication;
import com.group5.deliveryservice.dto.UserDto;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.function.Function;

@Component
public class CustomerAuthenticationServiceRequests {

    private static final String CUSTOMER_AUTHENTICATION_SERVICE_BASE_URL = "http://customer-authentication-service:8081/api/cas";
    private static final Function<String, String> getCustomerAuthenticationServiceFetchUserUrl = id -> CUSTOMER_AUTHENTICATION_SERVICE_BASE_URL + "/users/" + id;

    public CustomerAuthenticationServiceRequests() {
    }

    public static UserDto getUserDetails(String userId) {
        var url = getCustomerAuthenticationServiceFetchUserUrl.apply(userId);
        return DeliveryServiceApplication.getRestTemplate().getForObject(url, UserDto.class);
    }
}
