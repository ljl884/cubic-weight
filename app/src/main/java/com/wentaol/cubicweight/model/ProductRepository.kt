package com.wentaol.cubicweight.model

import com.wentaol.cubicweight.api.IProductService
import io.reactivex.Flowable
import javax.inject.Inject

class ProductRepository @Inject constructor(private val productService: IProductService) {
    fun getAllProducts(): Flowable<Product> = productService.getAllProducts()
}