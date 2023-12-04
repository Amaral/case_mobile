package br.com.omie.presentation.order_registration

sealed class ValidationEvent{
    object Success: ValidationEvent()
}

sealed class UiEvent {
    data class ShowSnackbar(val message: String): UiEvent()
    object SaveOrder: UiEvent()
}