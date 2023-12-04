package br.com.omie

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import br.com.omie.presentation.order_registration.OrderViewModel
import br.com.omie.presentation.order_registration.OrdersView
import br.com.omie.ui.theme.OmieTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class OrdersActivity : ComponentActivity() {

    companion object {
        fun newInstance(context: Context) = Intent(context, OrdersActivity::class.java)
    }

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OmieTheme {

                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {

                    val windowSize = calculateWindowSizeClass(this)

                    OrdersView(windowSize, ViewModelProvider(this)[OrderViewModel::class.java], onBack = {
                        finish()
                    })
                }
            }
        }
    }
}