package com.saga.coreapis.commands;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateShippingCommand {

    @TargetAggregateIdentifier
    private String shippingId;
    private String orderId;
    private String paymentId;
}
