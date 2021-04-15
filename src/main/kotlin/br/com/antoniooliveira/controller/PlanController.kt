package br.com.antoniooliveira.controller

import br.com.antoniooliveira.model.Plan
import br.com.antoniooliveira.repository.PlanRepository
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post

@Controller("plan")
class PlanController (private val planRepository: PlanRepository) {

    @Get
    fun listAll(): List<Plan> {
        return planRepository.findAll()
    }

    @Post
    fun save(plan: Plan): HttpResponse<Plan> {
        return HttpResponse.created(planRepository.save(plan))
    }
}