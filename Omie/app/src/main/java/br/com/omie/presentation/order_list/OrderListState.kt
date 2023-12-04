package br.com.omie.presentation.order_list

import br.com.omie.domain.model.Order

data class OrderListState(
    val list:List<Order> = emptyList()
)
