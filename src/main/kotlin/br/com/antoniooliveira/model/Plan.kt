package br.com.antoniooliveira.model

import io.micronaut.core.annotation.Introspected
import javax.persistence.*

@Entity
@Introspected
data class Plan (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @Column
    val plan_name: String,

    @Column
    val value: Long,

    @Column
    val active: Boolean
)
