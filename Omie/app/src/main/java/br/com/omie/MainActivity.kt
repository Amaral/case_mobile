package br.com.omie

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import br.com.omie.presentation.home.HomeViewModel
import br.com.omie.ui.theme.OmieTheme
import br.com.omie.util.OnLifecycleEvent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OmieTheme {
                val viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
                val state = viewModel.state
                val context = LocalContext.current

                OnLifecycleEvent { _, event ->
                    when (event) {
                        Lifecycle.Event.ON_RESUME -> { viewModel.refreshTotalValue() }
                        else                      -> {  }
                    }
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Button(onClick = { startActivity(OrdersActivity.newInstance(context)) }) {
                            Text(getString(R.string.make_order))
                        }
                        Spacer(modifier = Modifier.size(32.dp))
                        Text(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.tertiaryContainer)
                                .padding(16.dp),
                            text = buildAnnotatedString {
                                append(getString(R.string.total_orders))
                                withStyle(style = SpanStyle(fontWeight = FontWeight.SemiBold)) {
                                    append(stringResource(id = R.string.currency,state.orderTotalValue))
                                }
                            },
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                        Spacer(modifier = Modifier.size(32.dp))
                        Button(onClick = { startActivity(OrderListActivity.newInstance(context)) }) {
                            Text(getString(R.string.list_orders))
                        }
                    }
                }
            }
        }
    }
}

