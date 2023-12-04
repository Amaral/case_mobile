package br.com.omie.presentation.order_registration

import br.com.omie.domain.model.Order
import br.com.omie.util.UiText

data class OrderState(
    val order: Order = Order(),
    val totalOrderValue:String = "0,00",
    val totalOrderQuantity:String = "0",
    val clientNameError: UiText? = null,
)
