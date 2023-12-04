package br.com.omie.domain.use_case

import br.com.omie.util.UiText

data class ValidationResult(
    val successful:Boolean,
    val errorMessage:UiText? = null
)
