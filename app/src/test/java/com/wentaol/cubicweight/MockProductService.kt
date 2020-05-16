package com.wentaol.cubicweight

import com.wentaol.cubicweight.api.IProductService
import com.wentaol.cubicweight.model.Product
import com.wentaol.cubicweight.model.ProductPage
import io.reactivex.Flowable
import io.reactivex.Single

class MockProductService : IProductService {
    override fun getAllProducts(): Flowable<Product> {
        return Flowable.just(null)
    }

    override fun getProductPage(endpoint: String): Single<ProductPage> {
        return Single.just(null)
    }
}