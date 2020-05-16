package com.wentaol.cubicweight.api

import com.wentaol.cubicweight.model.ProductPage
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Url

interface ProductApi {
    @GET
    fun getProducts(
        @Url path: String
    ) : Single<ProductPage>
}