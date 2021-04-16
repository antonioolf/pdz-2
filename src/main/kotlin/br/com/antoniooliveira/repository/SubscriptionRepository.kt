package br.com.antoniooliveira.repository

import br.com.antoniooliveira.model.Subscription
import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository

@Repository
interface SubscriptionRepository: JpaRepository<Subscription, Long> {}