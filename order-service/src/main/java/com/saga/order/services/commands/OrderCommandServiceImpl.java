package com.saga.order.services.commands;

import com.saga.coreapis.commands.CreateOrderCommand;
import com.saga.order.aggregates.OrderStatus;
import com.saga.order.dtos.OrderCreateDTO;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class OrderCommandServiceImpl implements OrderCommandService {

    @Autowired
    private CommandGateway commandGateway;

    @Override
    public CompletableFuture<String> createOrder(OrderCreateDTO orderCreateDTO) {
        return commandGateway.send(
                new CreateOrderCommand(
                        UUID.randomUUID().toString(),
                        orderCreateDTO.getItemType(),
                        orderCreateDTO.getPrice(),
                        orderCreateDTO.getCurrency(),
                        String.valueOf(OrderStatus.CREATED)
                )
        );
    }
}
