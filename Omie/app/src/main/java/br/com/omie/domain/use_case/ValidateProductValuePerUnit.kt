package br.com.omie.domain.use_case

import br.com.omie.R
import br.com.omie.util.UiText

class ValidateProductValuePerUnit {

    fun execute(valuePerUnit:String? = null): ValidationResult {

        if (valuePerUnit != null) {
            try {
                val parsedValuePerUnit = valuePerUnit.toDouble()
                if (parsedValuePerUnit == 0.0) {
                    return ValidationResult(
                        successful = false,
                        errorMessage = UiText.StringResource(R.string.error_product_value_per_unit)
                    )
                }

                return ValidationResult(successful = true)

            } catch (nfe: NumberFormatException) {
                return ValidationResult(
                    successful = false,
                    errorMessage = UiText.StringResource(R.string.error_product_value_per_unit_format)
                )
            }
        }

        return ValidationResult(
            successful = false,
            errorMessage = UiText.StringResource(R.string.error_product_value_per_unit)
        )


    }
}