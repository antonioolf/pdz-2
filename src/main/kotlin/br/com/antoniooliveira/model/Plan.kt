package br.com.antoniooliveira.model

import io.micronaut.core.annotation.Introspected
import javax.persistence.*
import java.util.*

@Entity
@Introspected
data class Plan (
    @Id
    val id: UUID = UUID.randomUUID(),

    @Column
    val plan_name: String,

    @Column
    val value: Long,

    @Column
    val active: Boolean
)
