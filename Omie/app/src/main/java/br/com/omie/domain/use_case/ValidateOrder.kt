package br.com.omie.domain.use_case

import br.com.omie.R
import br.com.omie.domain.model.Order
import br.com.omie.util.UiText

class ValidateOrder {

    fun execute(order:Order): ValidationResult {
        if(order.listProducts.orderProductList.isEmpty()){
           return ValidationResult(
               successful = false,
               errorMessage = UiText.StringResource(R.string.error_empty_products)
           )
        }
        return ValidationResult(successful = true)
    }
}