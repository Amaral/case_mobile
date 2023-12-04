package br.com.omie.domain.use_case

data class OrderUseCases(
    val addOrder: AddOrder,
    val deleteOrder:DeleteOrder,
    val getIDFromNewOrder: GetIDFromNewOrder,
    val validateClientName: ValidateClientName,
    val validateProductName: ValidateProductName,
    val validateProductQuantity: ValidateProductQuantity,
    val validateProductValuePerUnit: ValidateProductValuePerUnit,
    val calculateProductTotalValue: CalculateProductTotalValue,
    val formatCurrencyFromString: FormatCurrencyFromString,
    val calculateOrder: CalculateOrder,
    val validateOrder:ValidateOrder,
)
