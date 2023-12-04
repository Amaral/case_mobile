package br.com.omie.presentation.order_registration

sealed class RegistrationOrderEvent {
    data class NameClientChanged(val name:String): RegistrationOrderEvent()
    data class NameProductChanged(val name:String): RegistrationOrderEvent()
    data class QuantityProductChanged(val quantity:String): RegistrationOrderEvent()
    data class ValuePerUnitProductChanged(val valuePerUnit:String): RegistrationOrderEvent()
    object SubmitProduct: RegistrationOrderEvent()
    object SaveOrder: RegistrationOrderEvent()

}
