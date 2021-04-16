package br.com.antoniooliveira.controller

import br.com.antoniooliveira.model.Subscription
import br.com.antoniooliveira.repository.SubscriptionRepository
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.*
import java.util.*

@Controller("subscription")
class SubscriptionController (private val subscriptionRepository: SubscriptionRepository) {

    @Post
    fun create(subscription: Subscription): HttpResponse<Subscription> {
        subscription.renewal_days = 30
        subscription.active = true

        val today = Date()
        val calendar = Calendar.getInstance()
        calendar.time = today
        calendar.add(Calendar.DATE, 30)

        subscription.start_subscription = today
        subscription.end_subscription = calendar.time
        subscription.next_renewal_date = calendar.time

        return HttpResponse.created(subscriptionRepository.save(subscription))
    }

    @Get("/{id}")
    fun read(id: Long): HttpResponse<Subscription> {
        val subscription = subscriptionRepository.findById(id)

        if (subscription.isEmpty) {
            return HttpResponse.noContent()
        }

        return HttpResponse.created(subscription.get())
    }

    @Put
    fun update(subscription: Subscription): HttpResponse<Subscription> {
        return HttpResponse.created(subscriptionRepository.update(subscription))
    }

    @Delete
    fun delete(id: Long): HttpResponse<Subscription>  {
        subscriptionRepository.deleteById(id)
        return HttpResponse.ok()
    }
}