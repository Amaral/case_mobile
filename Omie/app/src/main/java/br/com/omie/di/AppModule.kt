package br.com.omie.di

import android.app.Application
import androidx.room.Room
import br.com.omie.data.data_source.OrderDatabase
import br.com.omie.data.repository.OrderRepositoryImpl
import br.com.omie.domain.repository.OrderRepository
import br.com.omie.domain.use_case.AddOrder
import br.com.omie.domain.use_case.CalculateOrder
import br.com.omie.domain.use_case.CalculateProductTotalValue
import br.com.omie.domain.use_case.DeleteOrder
import br.com.omie.domain.use_case.FormatCurrencyFromString
import br.com.omie.domain.use_case.GetIDFromNewOrder
import br.com.omie.domain.use_case.GetOrderList
import br.com.omie.domain.use_case.GetOrdersTotalValue
import br.com.omie.domain.use_case.OrderListUseCases
import br.com.omie.domain.use_case.OrderUseCases
import br.com.omie.domain.use_case.ValidateClientName
import br.com.omie.domain.use_case.ValidateOrder
import br.com.omie.domain.use_case.ValidateProductName
import br.com.omie.domain.use_case.ValidateProductQuantity
import br.com.omie.domain.use_case.ValidateProductValuePerUnit
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNoteDatabase(app: Application): OrderDatabase {
        return Room.databaseBuilder(
            app,
            OrderDatabase::class.java,
            OrderDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideNoteRepository(db: OrderDatabase): OrderRepository {
        return OrderRepositoryImpl(db.orderDao)
    }

    @Provides
    @Singleton
    fun provideOrderUseCases(repository: OrderRepository): OrderUseCases {
        return OrderUseCases(
            addOrder = AddOrder(repository),
            deleteOrder = DeleteOrder(repository),
            getIDFromNewOrder = GetIDFromNewOrder(repository),
            validateClientName = ValidateClientName(),
            validateProductName = ValidateProductName(),
            validateProductQuantity = ValidateProductQuantity(),
            validateProductValuePerUnit = ValidateProductValuePerUnit(),
            calculateProductTotalValue = CalculateProductTotalValue(),
            formatCurrencyFromString = FormatCurrencyFromString(),
            calculateOrder = CalculateOrder(),
            validateOrder = ValidateOrder()
        )
    }

    @Provides
    @Singleton
    fun provideGetOrdersTotalValue(repository: OrderRepository): GetOrdersTotalValue {
        return GetOrdersTotalValue(
            orderRepository = repository,
            formatCurrencyFromString = FormatCurrencyFromString(),
            calculateOrder = CalculateOrder()
        )
    }

    @Provides
    @Singleton
    fun provideGetOrderList(repository: OrderRepository): GetOrderList {
        return GetOrderList(
            repository = repository,

        )
    }
    @Provides
    @Singleton
    fun provideOrderListUseCases(repository: OrderRepository): OrderListUseCases {
        return OrderListUseCases(
            getOrderList = GetOrderList(repository),
            formatCurrencyFromString = FormatCurrencyFromString(),
            calculateOrder = CalculateOrder())
    }
}