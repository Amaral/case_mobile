package br.com.omie.domain.repository

import br.com.omie.domain.model.Order

interface OrderRepository {
    suspend fun getOrders(): List<Order>
    suspend fun getLastEmptyOrder():Order?
    suspend fun insertOrder(order: Order)
    suspend fun insertEmptyOrder(order: Order):Long
    suspend fun deleteOrder(order: Order)
}