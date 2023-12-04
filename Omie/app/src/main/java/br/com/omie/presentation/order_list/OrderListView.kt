package br.com.omie.presentation.order_list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderListView(viewModel: OrderListViewModel, onBack: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
    ) {

        val COLUMN1_WEIGHT = .15f
        val COLUMN2_WEIGHT = .3f

        Scaffold(modifier = Modifier
            .fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = { Text("Lista de pedidos") },
                    navigationIcon = {
                        IconButton(onClick = { onBack() }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Go Back"
                            )
                        }
                    }
                )
            }) { values ->

            val state = viewModel.state
            if (state.list.isNotEmpty()) {
                LazyColumn(
                    contentPadding = PaddingValues(
                        top = 16.dp,
                        bottom = 16.dp,
                    ),
                    modifier = Modifier.padding(values),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(state.list) { order ->
                        Column(
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.tertiaryContainer)
                                .padding(16.dp)
                        ) {
                            Text(
                                text = "Pedido #${order.id}",
                                style = MaterialTheme.typography.bodySmall
                            )
                            Text(
                                text = "Nome do cliente: ${order.nameClient}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row {
                                Text(
                                    text = "Produto",
                                    modifier = Modifier.weight(COLUMN2_WEIGHT),
                                    textAlign = TextAlign.Left,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = "Qtd",
                                    modifier = Modifier.weight(COLUMN1_WEIGHT),
                                    textAlign = TextAlign.Left,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = "VU",
                                    modifier = Modifier.weight(COLUMN2_WEIGHT),
                                    textAlign = TextAlign.Right,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = "VT",
                                    modifier = Modifier.weight(COLUMN2_WEIGHT),
                                    textAlign = TextAlign.Right,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                            for (product in order.listProducts.orderProductList) {
                                Row {
                                    Text(
                                        text = product.name,
                                        modifier = Modifier.weight(
                                            COLUMN2_WEIGHT
                                        ),
                                        textAlign = TextAlign.Left,
                                        style = MaterialTheme.typography.titleSmall
                                    )
                                    Text(
                                        text = product.quantity,
                                        modifier = Modifier.weight(
                                            COLUMN1_WEIGHT
                                        ),
                                        textAlign = TextAlign.Left,
                                        style = MaterialTheme.typography.titleSmall
                                    )
                                    Text(
                                        text = "R$ ${product.valuePerUnitFormatted}",
                                        modifier = Modifier.weight(
                                            COLUMN2_WEIGHT
                                        ),
                                        textAlign = TextAlign.Right,
                                        style = MaterialTheme.typography.titleSmall
                                    )
                                    Text(
                                        text = "R$ ${product.valueTotal}",
                                        modifier = Modifier.weight(
                                            COLUMN2_WEIGHT
                                        ),
                                        textAlign = TextAlign.Right,
                                        style = MaterialTheme.typography.titleSmall
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Valor total do pedido: R$ " + viewModel.calculateOrder(
                                    order.listProducts.orderProductList
                                ),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold
                            )

                        }

                    }
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Nenhum pedido cadastrado!")
                }
            }
        }
    }
}