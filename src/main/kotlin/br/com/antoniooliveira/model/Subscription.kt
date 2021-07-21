package br.com.antoniooliveira.model

import io.micronaut.core.annotation.Introspected
import java.util.*
import javax.persistence.*

@Entity
@Introspected
data class Subscription (
    @Id
    val id: UUID = UUID.randomUUID(),

    @Column
    val id_customer: UUID,

    @Column
    val id_plan: UUID,

    @Column
    var renewal_days: Int?,

    @Column
    var active: Boolean?,

    @Column
    var start_subscription: Date?,

    @Column
    var end_subscription: Date?,

    @Column
    var next_renewal_date: Date?,
)

