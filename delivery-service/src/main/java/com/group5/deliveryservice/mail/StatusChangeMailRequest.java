package com.group5.deliveryservice.mail;

import com.google.common.collect.ImmutableMap;
import com.group5.deliveryservice.model.DeliveryStatus;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

public class StatusChangeMailRequest implements MailRequest {
    private final DeliveryStatus deliveryStatus;
    private final List<String> trackingIds;

    private final String BODY_PREFIX = "Dear Customer, \n";
    private final String BODY_SUFFIX = "\n\nWith kind regards\nASEDelivery";
    private final String SUBJECT_PREFIX = "ASEDelivery:";
    private final ImmutableMap<DeliveryStatus, String> STATUS_SUBJECT = ImmutableMap.of(
            DeliveryStatus.CREATED, String.format("%s New order is created", SUBJECT_PREFIX),
            DeliveryStatus.DEPOSITED, String.format("%s Your order is now deposited", SUBJECT_PREFIX),
            DeliveryStatus.DELIVERED, String.format("%s Your order is delivered", SUBJECT_PREFIX)
    );

    private final ImmutableMap<DeliveryStatus, Function<String, String>> STATUS_MAIL_BODY = ImmutableMap.of(
            DeliveryStatus.CREATED, (trackingNumber) -> String.format("%sYour order is on the way. You can use the following tracking code to track it: %s. %s", BODY_PREFIX, trackingNumber, BODY_SUFFIX),
            DeliveryStatus.DEPOSITED, (__) -> String.format("%sYour order was deposited in the pick up box. %s", BODY_PREFIX, BODY_SUFFIX),
            DeliveryStatus.DELIVERED, (__) -> String.format("%sYour order was delivered. %s", BODY_PREFIX, BODY_SUFFIX));

    public StatusChangeMailRequest(final DeliveryStatus deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
        this.trackingIds = new LinkedList<>();
    }

    public StatusChangeMailRequest(final DeliveryStatus deliveryStatus, String trackingId) {
        this.deliveryStatus = deliveryStatus;
        trackingIds = new LinkedList<>();
        this.trackingIds.add(trackingId);
    }

    public StatusChangeMailRequest(final DeliveryStatus deliveryStatus, List<String> trackingIds) {
        this.deliveryStatus = deliveryStatus;
        this.trackingIds = trackingIds;
    }

    @Override
    public String getMailSubject() {
        return STATUS_SUBJECT.get(deliveryStatus);
    }

    @Override
    public String getMailBody() {
        String t = String.join(", ", trackingIds);
        return STATUS_MAIL_BODY.get(deliveryStatus).apply(t);
    }

    @Override
    public String getBodyPrefix() {
        return this.BODY_PREFIX;
    }

    @Override
    public String getBodySuffix() {
        return this.BODY_SUFFIX;
    }
}
