package com.archivision.payment.model;

import java.io.Serializable;

public record PaymentEvent(String paymentId, String status, String amount) implements Serializable {

}
