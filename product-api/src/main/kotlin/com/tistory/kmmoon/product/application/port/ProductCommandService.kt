package com.tistory.kmmoon.product.application.port

import com.tistory.kmmoon.core.exception.CommonResponse
import com.tistory.kmmoon.product.InventoryEntity
import com.tistory.kmmoon.product.application.port.`in`.ProductCreateUseCase
import com.tistory.kmmoon.product.application.port.`in`.ProductDeleteUseCase
import com.tistory.kmmoon.product.application.port.`in`.ProductModifyUseCase
import com.tistory.kmmoon.product.application.port.out.*
import com.tistory.kmmoon.product.domain.Product
import com.tistory.kmmoon.product.domain.mapper.ProductMapper
import com.tistory.kmmoon.product.domain.request.ProductCreateRequest
import com.tistory.kmmoon.product.domain.request.ProductModifyRequest
import org.springframework.stereotype.Component

@Component
class ProductCommandService (
  val queryProductPort: QueryProductPort,
  val createProductPort: CreateProductPort,
  val modifyProductPort: ModifyProductPort,
  val deleteProductPort: DeleteProductPort,
  val createInventoryPort: CreateInventoryPort,
  val mapper: ProductMapper
): ProductCreateUseCase, ProductModifyUseCase, ProductDeleteUseCase {
  override fun create(userId: Long, productCreateRequest: ProductCreateRequest): CommonResponse<Product> {
    val inventoryEntity = createInventoryPort.create(InventoryEntity(
      quantity = productCreateRequest.quantity,
    ))

    val productEntity = mapper.createEntity(productCreateRequest)
    productEntity.userId = userId
    
    productEntity.inventory = inventoryEntity
    val response = mapper.toData(createProductPort.create(productEntity))
    return CommonResponse.success(response)
  }

  override fun modify(userId: Long, productModifyRequest: ProductModifyRequest): Product {
    val findById = queryProductPort.findById(productModifyRequest.id) ?: throw Exception()
    if(findById.userId != userId)
      throw Exception()

    val entity = mapper.modifyEntity(productModifyRequest)
    return mapper.toData(modifyProductPort.modify(entity))
  }

  override fun delete(userId: Long, productId: Long) {
    val entity = queryProductPort.findById(productId)?: throw Exception()
    if(entity.userId != userId)
      throw Exception()

    deleteProductPort.delete(entity)
  }

}