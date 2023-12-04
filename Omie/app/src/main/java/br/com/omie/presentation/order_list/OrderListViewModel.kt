package br.com.omie.presentation.order_list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.omie.domain.model.OrderProduct
import br.com.omie.domain.use_case.CalculateOrder
import br.com.omie.domain.use_case.FormatCurrencyFromString
import br.com.omie.domain.use_case.GetOrderList
import br.com.omie.domain.use_case.OrderListUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderListViewModel @Inject constructor(
   private val orderListUseCases: OrderListUseCases
) : ViewModel() {

    var state by mutableStateOf(OrderListState())
        private set

    init {
        viewModelScope.launch {
            state = state.copy(list = orderListUseCases.getOrderList())
        }

    }

    fun calculateOrder(list: List<OrderProduct>): String {
        return orderListUseCases.formatCurrencyFromString.execute(orderListUseCases.calculateOrder.execute(list).getOrNull()?.second.toString())
    }
}