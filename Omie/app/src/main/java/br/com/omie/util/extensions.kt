package br.com.omie.util

import androidx.compose.ui.Modifier
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

inline fun Modifier.conditional(
    condition: Boolean,
    ifTrue: Modifier.() -> Modifier,
    ifFalse: Modifier.() -> Modifier = { this },
): Modifier = if (condition) {
    then(ifTrue(Modifier))
} else {
    then(ifFalse(Modifier))
}

fun Double?.toCurrency(): String {
    val unusualSymbols = DecimalFormatSymbols(Locale("pt", "BR"));
    val format = DecimalFormat("###,###,##.##", unusualSymbols)
    val groupingSeparator: String = format.decimalFormatSymbols.groupingSeparator.toString()
    format.isDecimalSeparatorAlwaysShown = false
    format.minimumFractionDigits = 0
    var s = format.format(this ?: 0)

    val pos = s.lastIndexOf('.');
    if(pos != -1) s = s.substring(0,pos) + ',' + s.substring(pos+1)

    return s
}

fun String?.toIntOrZero(): Int {
    if (this != null) {
        return try {
            this.toInt()
        } catch (nfe: NumberFormatException) {
            0
        }
    }
    return 0
}