package br.com.omie.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import br.com.omie.domain.model.Order
import br.com.omie.domain.model.OrderProduct
import br.com.omie.util.MyTypeConverters

@TypeConverters(value = [MyTypeConverters::class])
@Database(
    entities = [Order::class],
    version = 1,
    exportSchema = false
)
abstract class OrderDatabase: RoomDatabase() {
    abstract val orderDao:OrderDao

    companion object {
        const val DATABASE_NAME = "order_db"
    }
}