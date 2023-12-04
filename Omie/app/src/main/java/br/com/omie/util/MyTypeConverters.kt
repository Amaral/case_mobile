package br.com.omie.util

import androidx.room.TypeConverter
import br.com.omie.domain.model.OrderProductList
import com.google.gson.Gson

class   MyTypeConverters {

    @TypeConverter
    fun fromOrderProductToJSON(orderProductList: OrderProductList): String {
        return Gson().toJson(orderProductList)
    }
    @TypeConverter
    fun fromJSONToOrderProduct(json: String): OrderProductList {
        return Gson().fromJson(json,OrderProductList::class.java)
    }
}