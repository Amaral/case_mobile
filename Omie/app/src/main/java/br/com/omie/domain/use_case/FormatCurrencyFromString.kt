package br.com.omie.domain.use_case

import br.com.omie.util.toCurrency

class FormatCurrencyFromString {

    fun execute(value:String): String {
        val parsedValue: Double = try {
            value.toDouble()
        } catch (nfe: NumberFormatException) {
            0.0
        }
        return parsedValue.toCurrency()
    }
}