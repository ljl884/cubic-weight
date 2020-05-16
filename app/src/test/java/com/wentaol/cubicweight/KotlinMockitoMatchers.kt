package com.wentaol.cubicweight

import org.mockito.Mockito
import org.mockito.stubbing.OngoingStubbing

fun <T> whenMock(methodCall: T): OngoingStubbing<T> {
    return Mockito.`when`(methodCall)
}