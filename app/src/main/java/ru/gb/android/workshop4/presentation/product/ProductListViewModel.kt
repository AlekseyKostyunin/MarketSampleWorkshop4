package ru.gb.android.workshop4.presentation.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.gb.android.workshop4.data.favorites.FavoriteEntity
import ru.gb.android.workshop4.domain.favorites.AddFavoriteUseCase
import ru.gb.android.workshop4.domain.favorites.ConsumeFavoritesUseCase
import ru.gb.android.workshop4.domain.favorites.RemoveFavoriteUseCase
import ru.gb.android.workshop4.domain.product.ConsumeProductsUseCase
import ru.gb.android.workshop4.marketsample.R
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val consumeProductsUseCase: ConsumeProductsUseCase,
    private val productStateFactory: ProductStateFactory,
    private val consumeFavoritesUseCase: ConsumeFavoritesUseCase,
    private val addFavoriteUseCase: AddFavoriteUseCase,
    private val removeFavoriteUseCase: RemoveFavoriteUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ProductsScreenState())
    val state: StateFlow<ProductsScreenState> = _state.asStateFlow()

    fun requestProducts() {
        combine(
            consumeProductsUseCase(),
            consumeFavoritesUseCase()
        ) { products, favorites ->
            products.map { product -> productStateFactory.create(product, favorites) }
        }
            .onStart {
                _state.update { screenState -> screenState.copy(isLoading = true) }
            }
            .onEach { productListState ->
                _state.update { screenState ->
                    screenState.copy(
                        isLoading = false,
                        productListState = productListState,
                    )
                }
            }
            .catch {
                _state.update { screenState ->
                    screenState.copy(
                        hasError = true,
                        errorRes = R.string.error_wile_loading_data,
                    )
                }
            }.launchIn(viewModelScope)

//        consumeProductsUseCase()
//            .map { products ->
//                products.map { product -> productStateFactory.create(product) }
//            }
//            .onStart {
//                _state.update { screenState -> screenState.copy(isLoading = true) }
//            }
//            .onEach { productListState ->
//
//                _state.update { screenState ->
//                    screenState.copy(
//                        isLoading = false,
//                        productListState = productListState,
//                    )
//                }
//            }
//            .catch {
//                _state.update { screenState ->
//                    screenState.copy(
//                        hasError = true,
//                        errorRes = R.string.error_wile_loading_data,
//                    )
//                }
//            }
//            .launchIn(viewModelScope)
    }

    fun refresh() {
        requestProducts()
    }

    fun errorHasShown() {
        _state.update { screenState -> screenState.copy(hasError = false) }
    }

    fun addToFavorites(favoriteId: String) {
        viewModelScope.launch {
            addFavoriteUseCase(FavoriteEntity(favoriteId))
        }
    }

    fun removeFromFavorites(favoriteId: String) {
        viewModelScope.launch {
            removeFavoriteUseCase(FavoriteEntity(favoriteId))
        }
    }
}
