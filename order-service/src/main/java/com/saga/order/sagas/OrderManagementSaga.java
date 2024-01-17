package com.saga.order.sagas;

import com.saga.coreapis.commands.CreateInvoiceCommand;
import com.saga.coreapis.commands.CreateShippingCommand;
import com.saga.coreapis.commands.UpdateOrderStatusCommand;
import com.saga.coreapis.events.InvoiceCreatedEvent;
import com.saga.coreapis.events.OrderCreatedEvent;
import com.saga.coreapis.events.OrderShippedEvent;
import com.saga.coreapis.events.OrderUpdatedEvent;
import com.saga.order.aggregates.OrderStatus;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.SagaLifecycle;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

@Saga
public class OrderManagementSaga {

    @Autowired
    private transient CommandGateway commandGateway;

    @StartSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void handle(OrderCreatedEvent orderCreatedEvent){
        String paymentId = UUID.randomUUID().toString();
        System.out.println("SAGA invocado");
        
        // asociamos la Saga
        SagaLifecycle.associateWith("paymentId", paymentId);
        System.out.println("Order ID: " + orderCreatedEvent.getOrderId());

        // enviamos los comandos
        commandGateway.send(
                new CreateInvoiceCommand(
                        paymentId,
                        orderCreatedEvent.getOrderId()
                )
        );
    }

    @SagaEventHandler(associationProperty = "paymentId")
    public void handle(InvoiceCreatedEvent invoiceCreatedEvent){
        String shippingId = UUID.randomUUID().toString();

        System.out.println("SAGA continuacion");

        // asociamos la Saga con shipping-service
        SagaLifecycle.associateWith("shipping", shippingId);

        // enviamos el comando de Shipping
        commandGateway.send(
                new CreateShippingCommand(
                        shippingId,
                        invoiceCreatedEvent.getOrderId(),
                        invoiceCreatedEvent.getPaymentId()
                )
        );

    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handler(OrderShippedEvent orderShippedEvent){
        commandGateway.send(
                new UpdateOrderStatusCommand(
                        orderShippedEvent.getOrderId(),
                        String.valueOf(OrderStatus.SHIPPED)
                )
        );
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handler(OrderUpdatedEvent orderUpdatedEvent){
        SagaLifecycle.end();
    }

}
