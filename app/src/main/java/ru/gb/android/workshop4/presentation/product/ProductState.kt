package ru.gb.android.workshop4.presentation.product

import android.content.Context

typealias ErrorProvider = (Context) -> String

data class ProductsScreenState(
    val isLoading: Boolean = false,
    val productListState: List<ProductState> = emptyList(),
    val hasError: Boolean = false,
    val errorRes: Int = 0,
)

data class ProductState(
    val id: String = "",
    val name: String = "",
    val image: String = "",
    val price: String = "",
)
