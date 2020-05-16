package com.wentaol.cubicweight.model

import java.math.BigDecimal
import java.math.RoundingMode

val Product.Companion.CUBIC_WEIGHT_CONVERSION_FACTOR: Double
    get() = 250.0

val Product.cubicWeight
    get() = (size.length.centimeterToMeterBigDecimal() * size.height.centimeterToMeterBigDecimal() *
            size.width.centimeterToMeterBigDecimal() * Product.CUBIC_WEIGHT_CONVERSION_FACTOR.toBigDecimal()).toDouble()

fun Double.centimeterToMeterBigDecimal() = this.toBigDecimal() * BigDecimal.valueOf(0.01)

fun Double.toBigDecimal(): BigDecimal = BigDecimal.valueOf(this)

operator fun BigDecimal.times(other: BigDecimal): BigDecimal = this.multiply(other)

fun Iterable<BigDecimal>.average(): Double {
    var sum: BigDecimal = BigDecimal.ZERO
    var count = 0
    for (element in this) {
        sum += element
        count++
    }
    return if (count == 0) 0.0 else sum.divide(count.toBigDecimal(), RoundingMode.CEILING).toDouble()
}
