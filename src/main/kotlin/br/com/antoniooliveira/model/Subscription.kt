package br.com.antoniooliveira.model

import io.micronaut.core.annotation.Introspected
import java.util.*
import javax.persistence.*

@Entity
@Introspected
data class Subscription (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @Column
    val id_customer: Long,

    @Column
    val id_plan: Long,

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

