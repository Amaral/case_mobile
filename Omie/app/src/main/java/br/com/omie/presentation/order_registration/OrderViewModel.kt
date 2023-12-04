package br.com.omie.presentation.order_registration

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.omie.domain.model.InvalidOrderException
import br.com.omie.domain.model.Order
import br.com.omie.domain.model.OrderProduct
import br.com.omie.domain.model.OrderProductList
import br.com.omie.domain.use_case.OrderUseCases
import br.com.omie.util.toCurrency
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val orderUseCases: OrderUseCases
) : ViewModel() {


    var stateOrder by mutableStateOf(OrderState())
        private set
    var stateProduct by mutableStateOf(RegistrationProductFormState())
        private set

    private val uiEventChannel = Channel<UiEvent>()
    val uiEvent = uiEventChannel.receiveAsFlow()

    private val validationEventChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            val order = stateOrder.order
            val id = orderUseCases.getIDFromNewOrder(order)
            stateOrder = stateOrder.copy(order = Order(id = id))
        }

    }

    fun onEvent(event: RegistrationOrderEvent) {
        when (event) {
            is RegistrationOrderEvent.NameProductChanged -> {
                stateProduct = stateProduct.copy(name = event.name)
            }

            is RegistrationOrderEvent.QuantityProductChanged -> {
                stateProduct = stateProduct.copy(
                    quantity = event.quantity,
                    valueTotal = orderUseCases.calculateProductTotalValue.execute(
                        event.quantity,
                        stateProduct.valuePerUnit
                    ).toCurrency()
                )
            }

            is RegistrationOrderEvent.ValuePerUnitProductChanged -> {
                stateProduct = stateProduct.copy(
                    valuePerUnit = event.valuePerUnit,
                    valueTotal = orderUseCases.calculateProductTotalValue.execute(
                        stateProduct.quantity,
                        event.valuePerUnit
                    ).toCurrency()
                )
            }

            RegistrationOrderEvent.SubmitProduct -> {
                submitProduct()
            }

            RegistrationOrderEvent.SaveOrder -> {
                saveOrder()
            }

            is RegistrationOrderEvent.NameClientChanged -> {
                val order = stateOrder.order.copy(nameClient = event.name)
                stateOrder = stateOrder.copy(
                    order = order
                )
            }
        }
    }

    private fun saveOrder() {
        val order = stateOrder.order.copy()
        val nameClient = orderUseCases.validateClientName.execute(order.nameClient)
        val validateOrder = orderUseCases.validateOrder.execute(order = order)
        val hasError = listOf(
            nameClient,
            validateOrder
        ).any { !it.successful }

        if (hasError) {
            stateOrder = stateOrder.copy(
                clientNameError = nameClient.errorMessage
            )
            if (!validateOrder.successful) {
                viewModelScope.launch {
                    uiEventChannel.send(
                        UiEvent.ShowSnackbar(
                            message = validateOrder.errorMessage.toString()
                        )
                    )
                }

            }
            return
        }


        viewModelScope.launch {
            try {
                orderUseCases.addOrder(order)
                uiEventChannel.send(UiEvent.SaveOrder)
                stateOrder = OrderState()
                stateProduct = RegistrationProductFormState()
            } catch (e: InvalidOrderException) {
                uiEventChannel.send(
                    UiEvent.ShowSnackbar(
                        message = e.message ?: "Couldn't save order"
                    )
                )
            }
        }
    }

    private fun submitProduct() {

        val nameProductResult = orderUseCases.validateProductName.execute(stateProduct.name)
        val quantityProductResult =
            orderUseCases.validateProductQuantity.execute(stateProduct.quantity)
        val valuePerUnitResult =
            orderUseCases.validateProductValuePerUnit.execute(stateProduct.valuePerUnit)

        val hasError = listOf(
            nameProductResult,
            quantityProductResult,
            valuePerUnitResult
        ).any { !it.successful }

        if (hasError) {
            stateProduct = stateProduct.copy(
                nameError = nameProductResult.errorMessage,
                quantityError = quantityProductResult.errorMessage,
                valuePerUnitError = valuePerUnitResult.errorMessage
            )
            return
        }
        viewModelScope.launch {
            addProduct()
            updateValuesOrder()
            validationEventChannel.send(ValidationEvent.Success)
        }
    }

    private fun addProduct() {
        var order = stateOrder.order.copy()
        val newProductList = ArrayList(order.listProducts.orderProductList)
        newProductList.add(
            OrderProduct(
                name = stateProduct.name,
                quantity = stateProduct.quantity,
                valuePerUnit = stateProduct.valuePerUnit,
                valueTotal = stateProduct.valueTotal,
                valuePerUnitFormatted = orderUseCases.formatCurrencyFromString.execute(stateProduct.valuePerUnit)
            )
        )
        stateProduct = RegistrationProductFormState()
        order = order.copy(listProducts = OrderProductList(orderProductList = newProductList))
        stateOrder = stateOrder.copy(order = order)
    }

    private fun updateValuesOrder() {
        val order = stateOrder.order.copy()
        val result = orderUseCases.calculateOrder.execute(order.listProducts.orderProductList)
        val quantity = result.getOrNull()?.first ?: 0
        val valueTotal = result.getOrNull()?.second ?: 0

        stateOrder = stateOrder.copy(
            totalOrderQuantity = quantity.toString(),
            totalOrderValue = orderUseCases.formatCurrencyFromString.execute(valueTotal.toString())
        )
    }

    fun deleteCurrentOrder() {
        viewModelScope.launch {
            orderUseCases.deleteOrder(stateOrder.order)
        }
    }
}
