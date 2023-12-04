package br.com.omie.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Order")
data class Order(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val nameClient: String = "",
    val listProducts: OrderProductList = OrderProductList()
)

data class OrderProductList(
    val orderProductList: List<OrderProduct> = emptyList()
)

class InvalidOrderException(message: String) : Exception(message)