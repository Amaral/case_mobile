package br.com.omie.presentation.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.omie.domain.use_case.GetOrdersTotalValue
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getOrdersTotalValue: GetOrdersTotalValue
) : ViewModel() {
    var state by mutableStateOf(HomeState())
        private set


    fun refreshTotalValue(){
        viewModelScope.launch {
            state = state.copy(orderTotalValue = getOrdersTotalValue())
        }
    }
}