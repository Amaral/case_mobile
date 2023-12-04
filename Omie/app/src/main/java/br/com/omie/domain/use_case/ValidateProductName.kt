package br.com.omie.domain.use_case

import br.com.omie.R
import br.com.omie.util.UiText

class ValidateProductName {

    fun execute(name:String? = null): ValidationResult {
        if(name.isNullOrBlank()){
           return ValidationResult(
               successful = false,
               errorMessage = UiText.StringResource(R.string.error_input_name_product)
           )
        }
        return ValidationResult(successful = true)
    }
}