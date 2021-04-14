package br.com.antoniooliveira.controller

import br.com.antoniooliveira.model.Plan
import br.com.antoniooliveira.repository.PlanRepository
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import javax.inject.Inject

@Controller("plan")
class PlanController (private val planRepository: PlanRepository) {

    @Get
    fun listAll(): List<Plan> {
        return planRepository.findAll()
    }

    @Post
    fun save(plan: Plan): Plan {
        return planRepository.save(plan)
    }
}