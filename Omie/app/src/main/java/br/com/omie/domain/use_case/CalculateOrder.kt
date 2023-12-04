package br.com.omie.domain.use_case

import br.com.omie.domain.model.Order
import br.com.omie.domain.model.OrderProduct
import br.com.omie.util.toIntOrZero

typealias OrderQuantity = Int
typealias OrderTotalValue = Double
typealias OrderResume = Pair<OrderQuantity,OrderTotalValue>

class CalculateOrder {

    fun execute(list:List<OrderProduct>): Result<OrderResume> {
        var quantity = 0
        var valueTotal = 0.0

        for( product in list){
            quantity += product.quantity.toIntOrZero()
            valueTotal += product.valuePerUnit.toIntOrZero() * product.quantity.toIntOrZero()
        }
        return Result.success(Pair(quantity,valueTotal))
    }
}