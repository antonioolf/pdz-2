package br.com.antoniooliveira.controller

import br.com.antoniooliveira.repository.SubscriptionRepository
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.util.*

@Controller("daily_charge")
class DailyChargeController (private val subscriptionRepository: SubscriptionRepository) {

    val endpointPayments = "https://run.mocky.io/v3/5aade899-0865-43b8-9bd2-86b29ed34902"

    @Get
    fun dailyCharge(): HttpResponse<String> {
        // TODO: Filtrar ativos e com renew_date = hoje
        val activeSubscriptions = subscriptionRepository.getActiveSubscriptions(20)

        for (subscription in listOf(activeSubscriptions)) {
            val sub = subscription!!.get()

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
                obj.put("value", 123) // TODO: Pegar valor do plan

                val client = OkHttpClient()
                val request: Request = Request.Builder()
                    .url(endpointPayments)
                    .build()

                val response: Response = client.newCall(request).execute()
                if (response.code() == 200) {
                    return HttpResponse.created(response.body()!!.string())
                } else if(response.code() == 400) {
                    return HttpResponse.serverError(response.body()!!.string())
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
                return HttpResponse.serverError(ex.message)
            }

            return HttpResponse.ok()
        }

        return HttpResponse.ok()
    }
}