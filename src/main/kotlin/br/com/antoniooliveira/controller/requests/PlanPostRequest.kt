package br.com.antoniooliveira.controller.requests

data class PlanPostRequest(
    val plan_name: String,
    val value: Long,
    val active: Boolean
)
