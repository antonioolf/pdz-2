package br.com.antoniooliveira.repository

import br.com.antoniooliveira.model.Plan
import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository
import java.util.*

@Repository
interface PlanRepository: JpaRepository<Plan, UUID> {}