package com.wentaol.cubicweight.model

import com.squareup.moshi.Json

data class ProductPage(
    var objects: List<Product>,
    @Json(name = "next") var nextEndpoint: String?
)