package br.com.antoniooliveira.controller

import br.com.antoniooliveira.model.Plan
import br.com.antoniooliveira.model.Subscription
import br.com.antoniooliveira.repository.SubscriptionRepository
import br.com.antoniooliveira.repository.PlanRepository
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.util.*

@Controller("daily_charge")
class DailyChargeController (private val subscriptionRepository: SubscriptionRepository, private val planRepository: PlanRepository) {

    // val endpointPayments = "https://run.mocky.io/v3/5aade899-0865-43b8-9bd2-86b29ed34902"
   val endpointPayments = "http://payments:8082/payment"

    @Get
    fun dailyCharge(): HttpResponse<String> {
        val activeSubscriptions = subscriptionRepository.getActiveSubscriptions()

        if(!activeSubscriptions.isPresent) {
            return HttpResponse.ok("Nenhuma subscription para ser renovada")
        }

        val updatedList = mutableListOf<Subscription>()
        for (subscription in listOf(activeSubscriptions)) {
            val sub = subscription.get()

            val today = Date()
            val calendar = Calendar.getInstance()
            calendar.time = today
            calendar.add(Calendar.DATE, sub.renewal_days!!)
            sub.next_renewal_date = calendar.time

            subscriptionRepository.update(sub)

            try {
                val obj = JSONObject()
                obj.put("id_customer", sub.id_customer)
                obj.put("id_subscription", sub.id)
                val plan : Optional<Plan> = planRepository.findById(sub.id_plan)
                if (plan.isPresent) {
                    obj.put("value", plan.get().value)
                }

                val JSON = MediaType.get("application/json; charset=utf-8")
                val body = RequestBody.create(JSON, obj.toString())

                val client = OkHttpClient()
                val request: Request = Request.Builder()
                    .url(endpointPayments)
                    .post(body)
                    .build()

                val response: Response = client.newCall(request).execute()
                if(response.code() in 200..299) {
                    updatedList.add(sub)
                }else if(response.code() == 400) {
                    return HttpResponse.serverError(response.body()!!.string())
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
                return HttpResponse.serverError(ex.message)
            }

            val result = updatedList.map { it.id }.joinToString(", ")
            return HttpResponse.ok("Subscriptions atualizadas: $result")
        }

        return HttpResponse.ok()
    }
}