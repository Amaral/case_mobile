package br.com.omie.presentation.order_registration

import br.com.omie.util.UiText

data class RegistrationProductFormState(
    val name:String = "",
    val nameError:UiText? = null,
    val quantity:String = "1",
    val quantityError: UiText? = null,
    val valuePerUnit:String = "",
    val valuePerUnitError:UiText? = null,
    val valueTotal:String = "R$ 0,00"
)
