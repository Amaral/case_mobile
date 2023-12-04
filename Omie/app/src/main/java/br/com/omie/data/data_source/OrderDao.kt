package br.com.omie.data.data_source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import br.com.omie.domain.model.Order
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao {


    @Query("SELECT * FROM `order`")
    suspend fun getOrders(): List<Order>

    @Query("SELECT * FROM `order` ORDER BY id DESC LIMIT 1")
    suspend fun getLastEmptyOrder(): Order

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: Order)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmptyOrder(order: Order):Long

    @Delete
    suspend fun deleteOrder(order: Order)
}