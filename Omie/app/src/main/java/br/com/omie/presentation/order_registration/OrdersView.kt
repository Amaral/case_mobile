package br.com.omie.presentation.order_registration

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.omie.R
import br.com.omie.domain.model.OrderProduct
import br.com.omie.util.CurrencyTransformation
import br.com.omie.util.ObserveAsEvents
import br.com.omie.util.conditional
import kotlinx.coroutines.launch

private const val COLUMN1_WEIGHT = .4f
private const val COLUMN2_WEIGHT = .15f
private const val COLUMN3_WEIGHT = .25f

private const val COLUMN1_COMPACT_WEIGHT = .25f
private const val COLUMN2_COMPACT_WEIGHT = .10f
private const val COLUMN3_COMPACT_WEIGHT = .25f

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun OrdersView(
    windowSizeClass: WindowSizeClass,
    viewModel: OrderViewModel = viewModel(),
    onBack: () -> Unit,
) {
    val context = LocalContext.current
    ObserveAsEvents(viewModel.validationEvents) { event ->
        when (event) {
            ValidationEvent.Success -> {
                Toast.makeText(context, context.getText(R.string.product_added), Toast.LENGTH_SHORT).show()
            }
        }
    }

    ObserveAsEvents(viewModel.uiEvent) { event ->
        when (event) {
            UiEvent.SaveOrder -> onBack()
            is UiEvent.ShowSnackbar -> Toast.makeText(context, event.message, Toast.LENGTH_SHORT)
                .show()
        }
    }

    val widthSizeClass by rememberUpdatedState(windowSizeClass.widthSizeClass)

    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(skipHiddenState = false)
    )
    val scope = rememberCoroutineScope()
    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = 0.dp,
        sheetContent = {
            AddProduct(isVertical = true, viewModel = viewModel) {
                scope.launch {
                    scaffoldState.bottomSheetState.hide()
                }
            }
        }) {
        Scaffold(modifier = Modifier
            .fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = { Text(stringResource(R.string.order_title)) },
                    navigationIcon = {
                        IconButton(onClick = { onBack() }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Go Back"
                            )
                        }
                    }
                )
            },
            bottomBar = {
                val isHorizontal = when (widthSizeClass) {
                    WindowWidthSizeClass.Expanded, WindowWidthSizeClass.Medium -> true
                    WindowWidthSizeClass.Compact -> false
                    else -> true
                }
                BottomBar(viewModel = viewModel, isHorizontal = isHorizontal, onCancel = {
                    viewModel.deleteCurrentOrder()
                    onBack()
                })
            }
        ) { values ->
            val isCompactCells = when (widthSizeClass) {
                WindowWidthSizeClass.Expanded, WindowWidthSizeClass.Medium -> false
                WindowWidthSizeClass.Compact -> true
                else -> true
            }
            val stateOrder = viewModel.stateOrder
            LazyColumn(
                modifier = Modifier
                    .padding(values)
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clip(shape = RoundedCornerShape(8.dp))

            ) {
                item {
                    val showFormAddOrder = when (widthSizeClass) {
                        WindowWidthSizeClass.Expanded, WindowWidthSizeClass.Medium -> true
                        WindowWidthSizeClass.Compact -> false
                        else -> true
                    }
                    AddOrder(showFormAddOrder, viewModel = viewModel, openForm = {
                        scope.launch {
                            scaffoldState.bottomSheetState.expand()
                        }
                    })
                }
                if (stateOrder.order.listProducts.orderProductList.isNotEmpty()) {
                    stickyHeader {
                        HeaderTableOrders(isCompactCells)
                    }

                    items(stateOrder.order.listProducts.orderProductList) {
                        RowOrder(it, isCompactCells = isCompactCells)
                    }
                }

            }
        }
    }
}

@Composable
private fun HeaderTableOrders(isCompactCells: Boolean) {
    val currentIsCompactCells by rememberUpdatedState(isCompactCells)
    val colorBackground = MaterialTheme.colorScheme.surfaceVariant
    val color = MaterialTheme.colorScheme.onSurface
    val textSize = if (currentIsCompactCells) 12.sp else 14.sp
    Row(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(
                colorBackground
            )
            .padding(16.dp, 16.dp, 16.dp, 16.dp)

    ) {
        TableCell(
            text = stringResource(R.string.name),
            weight = if (currentIsCompactCells) COLUMN1_COMPACT_WEIGHT else COLUMN1_WEIGHT,
            alignment = TextAlign.Left,
            color = color,
            fontSize = textSize,
            title = true
        )
        TableCell(
            text = stringResource(R.string.qtd),
            weight = if (currentIsCompactCells) COLUMN2_COMPACT_WEIGHT else COLUMN2_WEIGHT,
            alignment = TextAlign.Right,
            color = color,
            fontSize = textSize,
            title = true
        )
        TableCell(
            text = stringResource(R.string.price),
            weight = if (currentIsCompactCells) COLUMN3_COMPACT_WEIGHT else COLUMN3_WEIGHT,
            alignment = TextAlign.Right,
            color = color,
            fontSize = textSize,
            title = true
        )
        TableCell(
            text = stringResource(R.string.total),
            weight = if (currentIsCompactCells) COLUMN3_COMPACT_WEIGHT else COLUMN3_WEIGHT,
            alignment = TextAlign.Right,
            color = color,
            fontSize = textSize,
            title = true
        )
    }
    Divider(thickness = 2.dp, color = color)

}

@Composable
private fun RowOrder(product: OrderProduct, isCompactCells: Boolean) {

    val currentIsCompactCells by rememberUpdatedState(isCompactCells)
    val colorBackground = MaterialTheme.colorScheme.surfaceVariant
    val colorText = MaterialTheme.colorScheme.onSurfaceVariant
    val textSize = if (currentIsCompactCells) 12.sp else 16.sp
    Row(
        Modifier
            .fillMaxWidth()
            .background(colorBackground)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TableCell(
            text = product.name,
            weight = if (currentIsCompactCells) COLUMN1_COMPACT_WEIGHT else COLUMN1_WEIGHT,
            alignment = TextAlign.Left,
            fontSize = textSize,
            color = colorText
        )
        TableCell(
            text = product.quantity,
            weight = if (currentIsCompactCells) COLUMN2_COMPACT_WEIGHT else COLUMN2_WEIGHT,
            alignment = TextAlign.Right,
            fontSize = textSize,
            color = colorText
        )
        TableCell(
            text = stringResource(R.string.currency, product.valuePerUnitFormatted),
            weight = if (currentIsCompactCells) COLUMN3_COMPACT_WEIGHT else COLUMN3_WEIGHT,
            alignment = TextAlign.Right,
            fontSize = textSize,
            color = colorText
        )
        TableCell(
            text = stringResource(R.string.currency, product.valueTotal),
            weight = if (currentIsCompactCells) COLUMN3_COMPACT_WEIGHT else COLUMN3_WEIGHT,
            alignment = TextAlign.Right,
            fontSize = textSize,
            color = colorText
        )
    }
}

@Composable
fun RowScope.TableCell(
    text: String,
    weight: Float,
    color: Color,
    fontSize: TextUnit = 16.sp,
    alignment: TextAlign = TextAlign.Center,
    title: Boolean = false
) {
    Text(
        text = text,
        Modifier
            .weight(weight),
        fontSize = fontSize,
        fontWeight = if (title) FontWeight.SemiBold else FontWeight.Normal,
        textAlign = alignment,
        color = color
    )
}

@Composable
private fun AddOrder(showFormAddOrder: Boolean, viewModel: OrderViewModel, openForm: () -> Unit) {
    val currentShowFormAddOrder by rememberUpdatedState(showFormAddOrder)
    val stateOrder = viewModel.stateOrder
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.number_order, stateOrder.order.id),
            style = MaterialTheme.typography.titleSmall
        )
        Spacer(modifier = Modifier.size(12.dp))
        Text(
            text = stringResource(R.string.client),
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.size(12.dp))
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = stateOrder.order.nameClient,
            isError = stateOrder.clientNameError != null,
            onValueChange = { viewModel.onEvent(RegistrationOrderEvent.NameClientChanged(it)) },
            label = { Text(stringResource(R.string.name_client)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text
            ),
            supportingText = {
                if (stateOrder.clientNameError != null) {
                    Text(text = stateOrder.clientNameError.asString(context))
                }
            }
        )
        Spacer(modifier = Modifier.size(16.dp))
        if (currentShowFormAddOrder) {
            AddProduct(viewModel = viewModel)
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(onClick = { openForm() }) {
                    Text(stringResource(R.string.add_new_product))
                }
            }
            Spacer(modifier = Modifier.size(16.dp))
        }
    }
}

@Composable
private fun AddProduct(
    isVertical: Boolean = false,
    viewModel: OrderViewModel,
    onAdded: (() -> Unit)? = null
) {
    Text(
        modifier = Modifier.padding(start = if (isVertical) 16.dp else 0.dp),
        text = stringResource(R.string.product),
        style = MaterialTheme.typography.titleMedium
    )
    Spacer(modifier = Modifier.size(12.dp))
    if (!isVertical) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            InputsAddProduct(viewModel, Modifier.weight(1f))
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            InputsAddProduct(viewModel, Modifier.fillMaxWidth())
        }
    }

    Spacer(modifier = Modifier.size(16.dp))
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        val focusManager = LocalFocusManager.current
        Button(onClick = {
            focusManager.clearFocus()
            onAdded?.invoke()
            viewModel.onEvent(RegistrationOrderEvent.SubmitProduct)
        }) {
            Text(stringResource(R.string.add))
        }
    }
    Spacer(modifier = Modifier.size(16.dp))
}

@Composable
private fun InputsAddProduct(viewModel: OrderViewModel, modifier: Modifier = Modifier) {
    val state = viewModel.stateProduct
    val context = LocalContext.current
    TextField(
        modifier = modifier,
        value = state.name,
        onValueChange = { viewModel.onEvent(RegistrationOrderEvent.NameProductChanged(it)) },
        isError = state.nameError != null,
        label = {
            Text(
                "Nome do produto",
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next
        ),
        supportingText = {
            if (state.nameError != null) {
                Text(text = state.nameError.asString(context))
            }
        }
    )
    Spacer(modifier = Modifier.size(8.dp))
    TextField(
        modifier = modifier,
        value = state.quantity,
        onValueChange = {
            if (it.isDigitsOnly()) {
                viewModel.onEvent(RegistrationOrderEvent.QuantityProductChanged(it))
            }
        },
        isError = state.quantityError != null,
        label = { Text(stringResource(R.string.quantity), overflow = TextOverflow.Ellipsis, maxLines = 1) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Next
        ),
        supportingText = {
            if (state.quantityError != null) {
                Text(text = state.quantityError.asString(context), color = MaterialTheme.colorScheme.error)
            }
        }

    )
    Spacer(modifier = Modifier.size(8.dp))
    TextField(
        modifier = modifier,
        value = state.valuePerUnit,
        onValueChange = {
            if (it.isDigitsOnly()) {
                viewModel.onEvent(RegistrationOrderEvent.ValuePerUnitProductChanged(it))
            }
        },
        isError = state.valuePerUnitError != null,
        label = {
            Text(
                stringResource(R.string.value_unit),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Next
        ),
        supportingText = {
            if (state.valuePerUnitError != null) {
                Text(text = state.valuePerUnitError.asString(context), color = MaterialTheme.colorScheme.error)
            }
        },
        visualTransformation = CurrencyTransformation()
    )
    Spacer(modifier = Modifier.size(8.dp))
    Text(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.tertiaryContainer)
            .padding(16.dp),
        text = stringResource(R.string.valor_total, state.valueTotal),
        style = MaterialTheme.typography.titleMedium
    )
}

@Composable
private fun CardOrderResume(viewModel: OrderViewModel, fullWidth: Boolean = false) {
    val stateOrder = viewModel.stateOrder
    Column(
        modifier = Modifier
            .conditional(fullWidth, { fillMaxWidth() })
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.tertiaryContainer)
            .padding(16.dp),
    ) {
        Text(
            text = buildAnnotatedString {
                append(stringResource(R.string.quantity_total_itens))
                withStyle(style = SpanStyle(fontWeight = FontWeight.SemiBold)) {
                    append(stateOrder.totalOrderQuantity)
                }
            },
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onTertiaryContainer,
        )
        Text(
            text = buildAnnotatedString {
                append(stringResource(R.string.value_total_order))
                withStyle(style = SpanStyle(fontWeight = FontWeight.SemiBold)) {
                    append("R$ ${stateOrder.totalOrderValue}")
                }
            },
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onTertiaryContainer
        )
    }
}

@Composable
private fun SubmitButtons(viewModel: OrderViewModel, onCancel: () -> Unit) {
    Row(
        horizontalArrangement = Arrangement.End, modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TextButton(onClick = { onCancel() }) {
            Text(stringResource(R.string.cancel))
        }
        Spacer(modifier = Modifier.width(16.dp))
        Button(onClick = { viewModel.onEvent(RegistrationOrderEvent.SaveOrder) }) {
            Text(stringResource(R.string.save))
        }
    }
}

@Composable
private fun BottomBar(isHorizontal: Boolean, viewModel: OrderViewModel, onCancel: () -> Unit) {
    val currentIsHorizontal by rememberUpdatedState(isHorizontal)

    if (currentIsHorizontal) {
        Row(
            Modifier.padding(16.dp, 0.dp, 16.dp, 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            CardOrderResume(viewModel = viewModel, fullWidth = false)
            SubmitButtons(viewModel, onCancel)
        }
    } else {
        Column(
            Modifier.padding(16.dp, 0.dp, 16.dp, 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            CardOrderResume(viewModel = viewModel, fullWidth = true)
            Spacer(modifier = Modifier.size(8.dp))
            SubmitButtons(viewModel, onCancel)
        }
    }

}
