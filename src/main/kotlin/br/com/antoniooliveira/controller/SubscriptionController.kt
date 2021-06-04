package br.com.antoniooliveira.controller

import br.com.antoniooliveira.model.Subscription
import br.com.antoniooliveira.repository.PlanRepository
import br.com.antoniooliveira.repository.SubscriptionRepository
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.*
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.util.*


const val SUBSCRIPTION_TOTAL_DAYS = 365
const val RENEWAL_DAYS = 60

@Controller("subscription")
class SubscriptionController (private val subscriptionRepository: SubscriptionRepository, private val planRepository: PlanRepository) {
    val endpointPayments = "https://run.mocky.io/v3/5aade899-0865-43b8-9bd2-86b29ed34902"

    @Post
    fun create(subscription: Subscription): HttpResponse<Any> {
        if (!planRepository.existsById(subscription.id_plan)) {
            return HttpResponse.noContent()
        }

        subscription.renewal_days = RENEWAL_DAYS
        subscription.active = true

        val today = Date()
        val calendar = Calendar.getInstance()
        calendar.time = today
        calendar.add(Calendar.DATE, RENEWAL_DAYS)

        subscription.start_subscription = today
        subscription.next_renewal_date = calendar.time

        val calendarEnd = Calendar.getInstance()
        calendarEnd.time = today
        calendarEnd.add(Calendar.DATE, SUBSCRIPTION_TOTAL_DAYS)
        subscription.end_subscription = calendarEnd.time

        try {
            val obj = JSONObject()
            obj.put("id_customer", subscription.id_customer)
            obj.put("id_subscription", subscription.id)
            obj.put("value", 123) // TODO: Pegar valor do plan

            val client = OkHttpClient()
            val request: Request = Request.Builder()
                .url(endpointPayments)
                .build()

            val response: Response = client.newCall(request).execute()
            if (response.code() == 200) {
                return HttpResponse.created(subscriptionRepository.save(subscription))
            } else if(response.code() == 400) {
                return HttpResponse.serverError(response.body()!!.string())
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            return HttpResponse.serverError()
        }

        return HttpResponse.ok()
    }

    @Get("/{id}")
    fun read(id: Long): HttpResponse<Subscription> {
        val subscription = subscriptionRepository.findById(id)

        if (subscription.isEmpty) {
            return HttpResponse.noContent()
        }

        return HttpResponse.created(subscription.get())
    }

    @Get
    fun listAll(): List<Subscription> {
        return subscriptionRepository.findAll()
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