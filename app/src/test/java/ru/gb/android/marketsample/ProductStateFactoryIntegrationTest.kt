package ru.gb.android.marketsample

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import ru.gb.android.workshop4.data.favorites.FavoriteEntity
import ru.gb.android.workshop4.domain.product.Product
import ru.gb.android.workshop4.presentation.common.PriceFormatter
import ru.gb.android.workshop4.presentation.product.ProductState
import ru.gb.android.workshop4.presentation.product.ProductStateFactory

@RunWith(JUnit4::class)
class ProductStateFactoryIntegrationTestTest {

    @Mock
    private lateinit var priceFormatter: PriceFormatter

    @InjectMocks
    private lateinit var factory: ProductStateFactory

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun testCreate_ValidProductAndFavorites_ReturnsProductStateWithCorrectValues() {
        // Arrange
        val product = Product(id = "1", name = "Test Product", image = "test.jpg", price = 100.0)
        val favorites = listOf(FavoriteEntity(id = "1"))
        val expected = ProductState(id = "1", name = "Test Product", image = "test.jpg", price = "100,00", isFavorite = true)
        Mockito.`when`(priceFormatter.format(100.0)).thenReturn("100,00")

        // Act
        val actual = ProductStateFactory(priceFormatter).create(product, favorites)

        // Assert
        assertEquals(expected, actual)
    }

    @Test
    fun testCreate_InvalidProductAndFavorites_ReturnsProductStateWithCorrectValues() {
        // Arrange
        val product = Product(id = "2", name = "Test Product", image = "test.jpg", price = 100.0)
        val favorites = listOf(FavoriteEntity(id = "1"))
        val expected = ProductState(id = "2", name = "Test Product", image = "test.jpg", price = "100,00", isFavorite = false)
        Mockito.`when`(priceFormatter.format(100.0)).thenReturn("100,00")

        // Act
        val actual = ProductStateFactory(priceFormatter).create(product, favorites)

        // Assert
        assertEquals(expected, actual)
    }
}