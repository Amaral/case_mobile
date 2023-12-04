package br.com.omie.domain.use_case

import br.com.omie.domain.repository.OrderRepository

class GetOrdersTotalValue(
    private val orderRepository: OrderRepository,
    private val calculateOrder: CalculateOrder,
    private val formatCurrencyFromString: FormatCurrencyFromString
) {

    suspend operator fun invoke(): String {

        var total = 0.0
        orderRepository.getOrders().map { order ->
            calculateOrder.execute(order.listProducts.orderProductList).map {
                total += it.second
            }
        }
        return formatCurrencyFromString.execute(total.toString())
    }
}