package com.example.complimaton.managers
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

data class ComplimentResponse(
    val compliment: String
)

interface ComplimentApiService {
    @GET("api")
    fun getCompliment(): Call<ComplimentResponse>
}

class ComplimentManager  {
    private val baseUrl = "https://complimentr.com/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiService = retrofit.create(ComplimentApiService::class.java)

    fun fetchCompliment(callback: (String) -> Unit) {
        val call = apiService.getCompliment()
        call.enqueue(object : retrofit2.Callback<ComplimentResponse> {
            override fun onResponse(
                call: Call<ComplimentResponse>,
                response: retrofit2.Response<ComplimentResponse>
            ) {
                if (response.isSuccessful) {
                    val compliment = response.body()?.compliment ?: "No compliment available"
                    callback(compliment)
                } else {
                    callback("Failed to fetch compliment")
                }
            }

            override fun onFailure(call: Call<ComplimentResponse>, t: Throwable) {
                callback("Failed to fetch compliment")
            }
        })
    }
}