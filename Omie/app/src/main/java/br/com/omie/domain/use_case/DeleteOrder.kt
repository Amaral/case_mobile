package br.com.omie.domain.use_case

import br.com.omie.domain.model.Order
import br.com.omie.domain.repository.OrderRepository

class DeleteOrder(private val repository: OrderRepository) {

    suspend operator fun invoke(order: Order) {
        repository.deleteOrder(order)
    }
}