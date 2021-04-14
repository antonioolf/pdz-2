package br.com.antoniooliveira.repository

import br.com.antoniooliveira.model.Plan
import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository

@Repository
interface PlanRepository: JpaRepository<Plan, Long> {}