package ru.gb.android.marketsample

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import ru.gb.android.workshop4.data.favorites.FavoriteEntity
import ru.gb.android.workshop4.domain.product.Product
import ru.gb.android.workshop4.presentation.common.PriceFormatter
import ru.gb.android.workshop4.presentation.product.ProductStateFactory

@RunWith(JUnit4::class)
class ProductStateFactoryTest {

    private lateinit var factory: ProductStateFactory
    private lateinit var priceFormatter: PriceFormatter

    @Before
    fun setup() {
        priceFormatter = mock(PriceFormatter::class.java)
        factory = ProductStateFactory(priceFormatter)
    }

    @Test
    fun testCreate() {
        val product = Product("1", "Product Name", "image.jpg", 100.0)
        val favorites = listOf(FavoriteEntity("1"))

        `when`(priceFormatter.format(product.price)).thenReturn("100.00")

        val productState = factory.create(product, favorites)

        assertEquals("1", productState.id)
        assertEquals("Product Name", productState.name)
        assertEquals("image.jpg", productState.image)
        assertEquals("100.00", productState.price)
        assertTrue(productState.isFavorite)
    }

    @Test
    fun testCreateWithoutFavorites() {
        val product = Product("1", "Product Name", "image.jpg", 100.0)
        val favorites = emptyList<FavoriteEntity>()

        `when`(priceFormatter.format(product.price)).thenReturn("100.00")

        val productState = factory.create(product, favorites)

        assertEquals("1", productState.id)
        assertEquals("Product Name", productState.name)
        assertEquals("image.jpg", productState.image)
        assertEquals("100.00", productState.price)
        assertFalse(productState.isFavorite)
    }
}