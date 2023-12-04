package br.com.omie.domain.use_case

import br.com.omie.domain.model.InvalidOrderException
import br.com.omie.domain.model.Order
import br.com.omie.domain.repository.OrderRepository

class AddOrder(private val repository: OrderRepository) {

    @Throws(InvalidOrderException::class)
    suspend operator fun invoke(order: Order) {
        if (order.nameClient.isBlank()) {
            throw InvalidOrderException("The name of the client can't be empty.")
        }
        if (order.listProducts.orderProductList.isEmpty()) {
            throw InvalidOrderException("The order product list can't be empty")
        }
        repository.insertOrder(order)
    }
}