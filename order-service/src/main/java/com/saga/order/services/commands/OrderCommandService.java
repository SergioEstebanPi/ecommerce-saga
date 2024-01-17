package com.saga.order.services.commands;

import com.saga.order.dtos.OrderCreateDTO;

import java.util.concurrent.CompletableFuture;

public interface OrderCommandService {

    public CompletableFuture<String> createOrder(OrderCreateDTO orderCreateDTO);
}
