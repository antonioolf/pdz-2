package br.com.antoniooliveira.controller

import br.com.antoniooliveira.controller.requests.PlanPostRequest
import br.com.antoniooliveira.model.Plan
import br.com.antoniooliveira.repository.PlanRepository
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.*
import java.util.*

@Controller("plan")
class PlanController (private val planRepository: PlanRepository) {

    @Post
    fun create(plan: PlanPostRequest): HttpResponse<Plan> {
        val planToSave = Plan(plan_name = plan.plan_name, active = plan.active, value = plan.value)
        return HttpResponse.created(planRepository.save(planToSave))
    }

    @Get("/{id}")
    fun read(id : UUID): Plan {
        val plan = planRepository.findById(id)
        return plan.get()
    }

    @Put
    fun update(plan: Plan): HttpResponse<Plan> {
        return HttpResponse.created(planRepository.update(plan))
    }

    @Delete
    fun delete(id: UUID): HttpResponse<Plan>  {
        planRepository.deleteById(id)
        return HttpResponse.ok()
    }

    @Get
    fun listAll(): List<Plan> {
        return planRepository.findAll()
    }
}