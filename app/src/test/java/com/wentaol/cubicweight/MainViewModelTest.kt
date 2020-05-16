package com.wentaol.cubicweight

import com.wentaol.cubicweight.model.Product
import com.wentaol.cubicweight.model.ProductRepository
import com.wentaol.cubicweight.model.ProductSize
import com.wentaol.cubicweight.ui.MainViewModel
import io.reactivex.Flowable
import io.reactivex.observers.TestObserver
import io.reactivex.rxkotlin.toFlowable
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class MainViewModelTest {
    @Mock
    lateinit var mockProductRepository: ProductRepository

    lateinit var viewModel: MainViewModel
    private lateinit var testObserver: TestObserver<Any>

    private val defaultCategoryFilter = "category A"
    private val categories = listOf(defaultCategoryFilter, "category B", "category C")

    private val products = categories.flatMap {
        listOf(
            Product(it, "$it 1", 10.0, ProductSize(30.0, 40.0, 50.0)),
            Product(it, "$it 2", 5555.555, ProductSize(111.11, 222.222, 333.333)),
            Product(it, "$it 3", null, ProductSize(4.4, 5.5, 6.6))
        )
    }

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        viewModel = MainViewModel(mockProductRepository)
        viewModel.addCategoryFilter(defaultCategoryFilter)
        testObserver = TestObserver.create()

        whenMock(mockProductRepository.getAllProducts()).thenReturn(products.toFlowable())
    }

    @Test
    fun `When loadProductData() is called, if ProductRepository returns an error, observer should receive the error`() {
        val error = Throwable("error")
        whenMock(mockProductRepository.getAllProducts()).thenReturn(Flowable.error(error))
        viewModel.loadProductData().subscribeWith(testObserver)
        testObserver.awaitTerminalEvent()

        testObserver.assertError(error)
    }

    @Test
    fun `Given ProductRepository returns valid data, when loadProductData() is called, productList should emit all products with default category`() {
        viewModel.loadProductData().subscribeWith(testObserver)
        testObserver.awaitTerminalEvent()

        testObserver.assertComplete()
        viewModel.productList.test().assertValue { list ->
            list == products.filter { it.category == defaultCategoryFilter }
        }
    }

    @Test
    fun `Given ProductRepository returns valid data, when loadProductData() is called, if no category filter is applied, productList should emit empty list`() {
        viewModel.removeCategoryFilter(defaultCategoryFilter)
        viewModel.loadProductData().subscribeWith(testObserver)
        testObserver.awaitTerminalEvent()

        testObserver.assertComplete()
        viewModel.productList.test().assertValue { it.isEmpty() }
    }

    @Test
    fun `Given ProductRepository returns valid data, when loadProductData() is called, if multiple category filters are applied, productList should emit all products with filtered categories`() {
        val newCategoryFilter = categories[1]
        viewModel.addCategoryFilter(newCategoryFilter)
        viewModel.loadProductData().subscribeWith(testObserver)
        testObserver.awaitTerminalEvent()

        testObserver.assertComplete()
        viewModel.productList.test().assertValue { list ->
            list == products.filter { it.category == defaultCategoryFilter || it.category == newCategoryFilter }
        }
    }

    @Test
    fun `Given ProductRepository returns valid data, after loadProductData() is called, categoryList should return all categories`() {
        viewModel.loadProductData().subscribeWith(testObserver)
        testObserver.awaitTerminalEvent()

        assert(viewModel.categoryList.containsAll(categories))
    }
}