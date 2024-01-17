package com.saga.order.aggregates;

import com.saga.coreapis.commands.CreateOrderCommand;
import com.saga.coreapis.commands.UpdateOrderStatusCommand;
import com.saga.coreapis.events.OrderCreatedEvent;
import com.saga.coreapis.events.OrderUpdatedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import java.math.BigDecimal;

@Aggregate
public class OrderAggregate {

    @AggregateIdentifier
    private String orderId;
    private ItemType itemType;
    private BigDecimal price;
    private String currency;
    private OrderStatus orderStatus;

    public OrderAggregate() {} // requerido por axon

    @CommandHandler
    public OrderAggregate(CreateOrderCommand createOrderCommand) {
        AggregateLifecycle.apply(
                new OrderCreatedEvent(
                        createOrderCommand.getOrderId(),
                        createOrderCommand.getItemType(),
                        createOrderCommand.getPrice(),
                        createOrderCommand.getCurrency(),
                        createOrderCommand.getOrderStatus()
                )
        );
    }

    @EventSourcingHandler
    public void on(OrderCreatedEvent orderCreatedEvent){
        this.orderId = orderCreatedEvent.getOrderId();
        this.itemType = ItemType.valueOf(orderCreatedEvent.getItemType());
        this.price = orderCreatedEvent.getPrice();
        this.currency = orderCreatedEvent.getCurrency();
        this.orderStatus = OrderStatus.valueOf(orderCreatedEvent.getOrderStatus());
    }

    @CommandHandler
    public OrderAggregate(UpdateOrderStatusCommand updateOrderStatusCommand) {
        AggregateLifecycle.apply(
                new OrderUpdatedEvent(
                        updateOrderStatusCommand.getOrderId(),
                        updateOrderStatusCommand.getOrderStatus()
                )
        );
    }

    @EventSourcingHandler
    public void on(UpdateOrderStatusCommand updateOrderStatusCommand){
        this.orderId = updateOrderStatusCommand.getOrderId();
        this.orderStatus = OrderStatus.valueOf(updateOrderStatusCommand.getOrderStatus());
    }
}
