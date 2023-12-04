package br.com.omie.domain.use_case

class CalculateProductTotalValue {

    fun execute(quantity:String, valuePerUnit:String): Double {

        val parsedQuantity: Int = try {
            quantity.toInt()
        } catch (nfe: NumberFormatException) {
            0
        }
        val parsedValuePerUnit: Double = try {
            valuePerUnit.toDouble()
        } catch (nfe: NumberFormatException) {
            0.0
        }
        return parsedQuantity * parsedValuePerUnit
    }
}