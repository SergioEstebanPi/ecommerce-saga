package com.saga.payment.aggregates;

import com.saga.coreapis.commands.CreateInvoiceCommand;
import com.saga.coreapis.events.InvoiceCreatedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.modelling.command.TargetAggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;

@Aggregate
public class InvoiceAggregate {

    @TargetAggregateIdentifier
    private String paymentId;
    private String orderId;
    private InvoiceStatus invoiceStatus;

    public InvoiceAggregate() {}

    @CommandHandler
    public InvoiceAggregate(CreateInvoiceCommand createInvoiceCommand){
        AggregateLifecycle.apply(
                new InvoiceCreatedEvent(
                        createInvoiceCommand.getPaymentId(),
                        createInvoiceCommand.getOrderId()
                )
        );
    }

    @EventSourcingHandler
    public void on(InvoiceCreatedEvent invoiceCreatedEvent){
        this.paymentId = invoiceCreatedEvent.getPaymentId();
        this.orderId = invoiceCreatedEvent.getOrderId();
        this.invoiceStatus = InvoiceStatus.PAID;
    }

}
