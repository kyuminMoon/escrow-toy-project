package com.tistory.kmmoon.product.application.port.`in`

import com.tistory.kmmoon.core.exception.CommonResponse
import com.tistory.kmmoon.product.domain.Product
import com.tistory.kmmoon.product.domain.request.ProductCreateRequest

interface ProductCreateUseCase {
  fun create(userId: Long, productCreateRequest: ProductCreateRequest): CommonResponse<Product>
}