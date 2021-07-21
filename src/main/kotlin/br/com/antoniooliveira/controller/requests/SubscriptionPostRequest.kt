package br.com.antoniooliveira.controller.requests

import java.util.*

data class SubscriptionPostRequest(
    val id_customer: UUID,
    val id_plan: UUID,
    var renewal_days: Int?,
    var active: Boolean?,
    var start_subscription: Date?,
    var end_subscription: Date?,
    var next_renewal_date: Date?,
)
