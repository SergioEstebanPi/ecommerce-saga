package com.saga.coreapis.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderUpdatedEvent {

    private String orderId;
    private String orderStatus;
}
