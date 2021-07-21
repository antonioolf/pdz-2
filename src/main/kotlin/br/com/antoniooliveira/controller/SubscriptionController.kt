package br.com.antoniooliveira.controller

import br.com.antoniooliveira.controller.requests.SubscriptionPostRequest
import br.com.antoniooliveira.model.Plan
import br.com.antoniooliveira.model.Subscription
import br.com.antoniooliveira.repository.PlanRepository
import br.com.antoniooliveira.repository.SubscriptionRepository
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.*
import okhttp3.*
import okhttp3.RequestBody.create
import org.json.JSONObject
import java.util.*


const val SUBSCRIPTION_TOTAL_DAYS = 365
const val RENEWAL_DAYS = 60

@Controller("subscription")
class SubscriptionController (private val subscriptionRepository: SubscriptionRepository, private val planRepository: PlanRepository) {
//    val endpointPayments = "https://run.mocky.io/v3/5aade899-0865-43b8-9bd2-86b29ed34902"
    val endpointPayments = "http://payments:8082/payment"

    @Post
    fun create(request: SubscriptionPostRequest): HttpResponse<Any> {
        if (!planRepository.existsById(request.id_plan)) {
            return HttpResponse.noContent()
        }

        val today = Date()
        val calendar = Calendar.getInstance()
        calendar.time = today
        calendar.add(Calendar.DATE, RENEWAL_DAYS)

        val calendarEnd = Calendar.getInstance()
        calendarEnd.time = today
        calendarEnd.add(Calendar.DATE, SUBSCRIPTION_TOTAL_DAYS)

        val subscription = Subscription(
            id_customer = request.id_customer,
            id_plan = request.id_plan,
            renewal_days = RENEWAL_DAYS,
            active = true,
            start_subscription = today,
            next_renewal_date = calendar.time,
            end_subscription = calendarEnd.time
        )

        try {
            println("Customer ID: ${subscription.id_customer}")
            println("Subscription ID: ${subscription.id}")

            val requestBody = JSONObject()

            requestBody.put("id_customer", subscription.id_customer)
            requestBody.put("id_subscription", subscription.id)

            val plan : Optional<Plan> = planRepository.findById(subscription.id_plan)
            if (plan.isPresent) {
                requestBody.put("value", plan.get().value)
                println("Plan found: ${plan.get().value}")
            }

            val json = MediaType.get("application/json; charset=utf-8")
            val body = create(json, requestBody.toString())

            val client = OkHttpClient()
            val request2: Request = Request.Builder()
                .addHeader("Content-Type", "application/json; charset=utf8")
                .url(endpointPayments)
                .post(body)
                .build()

            val response: Response = client.newCall(request2).execute()
            print("Response code ${response.code()}\n")
            if (response.code() in 200..299) {
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
    fun read(id: UUID): HttpResponse<Subscription> {
        val subscription = subscriptionRepository.findById(id)

        if (!subscription.isPresent) {
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
    fun delete(id: UUID): HttpResponse<Subscription>  {
        subscriptionRepository.deleteById(id)
        return HttpResponse.ok()
    }
}