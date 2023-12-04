package br.com.omie.data.repository

import br.com.omie.data.data_source.OrderDao
import br.com.omie.domain.model.Order
import br.com.omie.domain.repository.OrderRepository

class OrderRepositoryImpl(private val dao: OrderDao) : OrderRepository {
    override suspend fun getOrders(): List<Order>{
        return dao.getOrders()
    }

    override suspend fun getLastEmptyOrder(): Order {
        return dao.getLastEmptyOrder()
    }

    override suspend fun insertOrder(order: Order) {
        dao.insertOrder(order)
    }

    override suspend fun insertEmptyOrder(order: Order): Long {
        return dao.insertEmptyOrder(order)
    }

    override suspend fun deleteOrder(order: Order) {
        dao.deleteOrder(order = order )
    }
}