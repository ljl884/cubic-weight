package com.wentaol.cubicweight.model

data class Product(
    var category: String,
    var title: String,
    var weight: Double?,
    var size: ProductSize
) {
    companion object
}

data class ProductSize(
    var width: Double,
    var length: Double,
    var height: Double
)