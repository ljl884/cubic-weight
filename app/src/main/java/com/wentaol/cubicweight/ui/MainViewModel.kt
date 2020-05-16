package com.wentaol.cubicweight.ui

import com.wentaol.cubicweight.model.Product
import com.wentaol.cubicweight.model.ProductRepository
import com.wentaol.cubicweight.model.average
import com.wentaol.cubicweight.model.cubicWeight
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

class MainViewModel @Inject constructor(private val productRepository: ProductRepository) {
    // region Instance variables

    private val productListInternal: BehaviorSubject<List<Product>> = BehaviorSubject.createDefault(listOf())
    private val averageCubicWeightInternal: BehaviorSubject<Double> = BehaviorSubject.createDefault(0.0)
    private var currentCategoryFilterInternal: MutableSet<String> = mutableSetOf()
    private val products: MutableList<Product> = mutableListOf()
    private val categories: MutableSet<String> = mutableSetOf()

    val productList: Observable<List<Product>>
        get() = productListInternal
    val categoryList: Set<String>
        get() = categories
    val averageCubicWeight: Observable<Double>
        get() = averageCubicWeightInternal
    val currentCategoryFilter: Set<String>
        get() = currentCategoryFilterInternal

    // endregion

    // region MainViewModel

    fun addCategoryFilter(filter: String) {
        currentCategoryFilterInternal.add(filter)
        onDataUpdated()
    }

    fun removeCategoryFilter(filter: String) {
        currentCategoryFilterInternal.remove(filter)
        onDataUpdated()
    }

    fun loadProductData(): Completable {
        products.clear()
        categories.clear()
        return productRepository.getAllProducts()
            .subscribeOn(Schedulers.io())
            .doOnNext {
                populate(it)
            }
            .doOnTerminate {
                onDataUpdated()
            }
            .ignoreElements()
    }

    private fun populate(product: Product) {
        products.add(product)
        categories.add(product.category)
    }

    private fun onDataUpdated() {
        val productList = products.filter { currentCategoryFilterInternal.contains(it.category) }
        averageCubicWeightInternal.onNext(productList.map { it.cubicWeight.toBigDecimal() }.average())
        productListInternal.onNext(productList)
    }

    // endregion
}