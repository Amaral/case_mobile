package br.com.omie.domain.use_case

import br.com.omie.domain.model.Order
import br.com.omie.domain.repository.OrderRepository

class GetIDFromNewOrder(private val repository: OrderRepository) {

    suspend operator fun invoke(order: Order): Long {

        val lastOrderEmpty = repository.getLastEmptyOrder()
        lastOrderEmpty?.let {
            if (it.listProducts.orderProductList.isEmpty()) {
                return it.id
            }
        }


        return repository.insertEmptyOrder(order)
    }
}