package br.com.antoniooliveira.repository

import br.com.antoniooliveira.model.Subscription
import io.micronaut.data.annotation.Query
import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository
import java.util.*

@Repository
interface SubscriptionRepository: JpaRepository<Subscription, UUID> {
    @Query(value = "SELECT s.* FROM subscription s where next_renewal_date = CURRENT_DATE and s.active = true", nativeQuery = true)
    fun  getActiveSubscriptions(): Optional<Subscription>
}