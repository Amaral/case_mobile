package br.com.omie.domain.use_case

import br.com.omie.domain.model.Order
import br.com.omie.domain.repository.OrderRepository

class GetOrderList(private val repository: OrderRepository) {

    suspend operator fun invoke(): List<Order> {

        return repository.getOrders().filter {
            it.nameClient.isNotEmpty()
        }

    }
}