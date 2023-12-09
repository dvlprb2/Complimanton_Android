package com.example.complimaton.managers
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface ComplimentApiService {
    @GET("compliments")
    fun getCompliment(): Call<String>
}

class ComplimentManager  {
    private val baseUrl = "https://8768zwfurd.execute-api.us-east-1.amazonaws.com/v1/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiService = retrofit.create(ComplimentApiService::class.java)

    fun fetchCompliment(callback: (String) -> Unit) {
        val call = apiService.getCompliment()
        call.enqueue(object : retrofit2.Callback<String> {
            override fun onResponse(
                call: Call<String>,
                response: retrofit2.Response<String>
            ) {
                if (response.isSuccessful) {
                    val compliment = response.body() ?: "No compliment available"
                    callback(compliment)
                } else {
                    callback("Failed to fetch compliment")
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                callback("Failed to fetch compliment")
            }
        })
    }
}