package com.tistory.kmmoon.order

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(name = "orders")
data class OrderEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    val id: Long,

    val userId: Long,
    val status: String,
    val totalAmount: BigDecimal,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,

    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER)
    val orderItems: MutableList<OrderItemEntity> = mutableListOf(),
    val paymentId: Long?
)