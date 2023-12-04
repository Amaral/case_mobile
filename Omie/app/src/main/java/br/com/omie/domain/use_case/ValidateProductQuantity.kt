package br.com.omie.domain.use_case

import br.com.omie.R
import br.com.omie.util.UiText

class ValidateProductQuantity {

    fun execute(quantity: String? = null): ValidationResult {

        if (quantity != null) {
            try {
                val parsedQuantity = quantity.toInt()
                if (parsedQuantity == 0) {
                    return ValidationResult(
                        successful = false,
                        errorMessage = UiText.StringResource(R.string.error_input_quantity)
                    )
                }

                return ValidationResult(successful = true)

            } catch (nfe: NumberFormatException) {
                return ValidationResult(
                    successful = false,
                    errorMessage = UiText.StringResource(R.string.error_quantity_string)
                )
            }
        }

        return ValidationResult(
            successful = false,
            errorMessage = UiText.StringResource(R.string.error_input_quantity)
        )
    }
}