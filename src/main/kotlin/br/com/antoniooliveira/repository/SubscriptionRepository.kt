package br.com.antoniooliveira.repository

import br.com.antoniooliveira.model.Subscription
import io.micronaut.data.annotation.Query
import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository
import java.util.*
import org.jetbrains.annotations.NotNull

@Repository
interface SubscriptionRepository: JpaRepository<Subscription, Long> {

/*    @Query("select s.id from subscription s where s.id = :id")
    fun getActiveSubscriptions(id: Long): Subscription*/

    @Query(value = "SELECT s.* FROM subscription s where renewal_days = :days", nativeQuery = true)
    fun  getActiveSubscriptions(days :Long): Optional<Subscription?>?
}