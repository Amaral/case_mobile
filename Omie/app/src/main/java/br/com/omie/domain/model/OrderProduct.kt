package br.com.omie.domain.model

import androidx.room.Entity


@Entity
data class OrderProduct(
    val name:String,
    val quantity:String,
    val valuePerUnit:String,
    val valuePerUnitFormatted:String,
    val valueTotal:String
)
