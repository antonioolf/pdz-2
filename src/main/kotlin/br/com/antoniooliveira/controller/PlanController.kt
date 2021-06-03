package br.com.antoniooliveira.controller

import br.com.antoniooliveira.model.Plan
import br.com.antoniooliveira.repository.PlanRepository
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.*

@Controller("plan")
class PlanController (private val planRepository: PlanRepository) {

    @Post
    fun create(plan: Plan): HttpResponse<Plan> {
        return HttpResponse.created(planRepository.save(plan))
    }

    @Get("/{id}")
    fun read(id :Long): Plan {
        val plan = planRepository.findById(id)
        return plan.get()
    }

    @Put
    fun update(plan: Plan): HttpResponse<Plan> {
        return HttpResponse.created(planRepository.update(plan))
    }

    @Delete
    fun delete(id: Long): HttpResponse<Plan>  {
        planRepository.deleteById(id)
        return HttpResponse.ok()
    }

    @Get
    fun listAll(): List<Plan> {
        return planRepository.findAll()
    }
}