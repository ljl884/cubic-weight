package com.wentaol.cubicweight.api

import com.wentaol.cubicweight.model.Product
import com.wentaol.cubicweight.model.ProductPage
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.processors.BehaviorProcessor
import retrofit2.Retrofit

interface IProductService {
    fun getAllProducts(): Flowable<Product>
    fun getProductPage(endpoint: String): Single<ProductPage>
}

class ProductService(private val builder: Retrofit.Builder) : IProductService {
    companion object {
        const val BASE_URL = "http://wp8m3he1wt.s3-website-ap-southeast-2.amazonaws.com"
        const val FIRST_PAGE_ENDPOINT = "/api/products/1"
    }

    private val service: ProductApi by lazy {
        val retrofit = builder
            .baseUrl(BASE_URL)
            .build()
        retrofit.create(ProductApi::class.java)
    }

    override fun getAllProducts(): Flowable<Product> {
        val processor = BehaviorProcessor.createDefault(FIRST_PAGE_ENDPOINT)
        return processor.concatMap { currentEndpoint ->
            getProductPage(currentEndpoint).toFlowable()
        }
            .doOnNext {
                val nextEndpoint = it.nextEndpoint
                if (nextEndpoint == null) {
                    processor.onComplete()
                } else {
                    processor.onNext(nextEndpoint)
                }
            }
            .concatMapIterable { it.objects }
    }

    override fun getProductPage(endpoint: String): Single<ProductPage> {
        return service.getProducts(endpoint)
    }
}