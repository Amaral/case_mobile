package br.com.omie.domain.use_case

data class OrderListUseCases(
    val getOrderList: GetOrderList,
    val calculateOrder: CalculateOrder,
    val formatCurrencyFromString: FormatCurrencyFromString
)
