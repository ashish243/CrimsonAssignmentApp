package com.crimson.assignment.api


import android.util.Log
import com.crimson.assignment.model.DetailRoot
import com.crimson.assignment.model.Search
import com.crimson.assignment.model.SearchList
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit


private const val TAG = "ApiService"

/*
this function query web service for data via retrofit library
if data retrieve successful i.e the [response.isSuccessful], meaning that data is retrieve success
else [onFailure] will handle the error
 */
fun getProductsResult(
    service: ProductService,
    customer_key: String,
    customer_secret: String,
    page: Int,
    itemsPerPage: String,
    onSuccess: (products: List<Search>) -> Unit,
    onError: (error: String) -> Unit
) {


    service.getProducts(customer_key, customer_secret, page, itemsPerPage ).enqueue(
        object : Callback<SearchList> {
            override fun onFailure(call: Call<SearchList>?, t: Throwable) {
                Log.d(TAG, "Error ${t.message}")
                onError(t.message ?: "unknown error")
            }

            override fun onResponse(
                call: Call<SearchList>?,
                response: Response<SearchList>
            ) {
                Log.d(TAG, "got a response $response")
                if (response.isSuccessful) {

                    if (response.body()!!.response){
                        Log.d(TAG, "got a response ${response.body()?.search}")
                        if (response.body()!!.search.isNullOrEmpty())
                            return

                        val products: List<Search> = response.body()!!.search
                        onSuccess(products)
                    }else {
                        val products: List<Search> = emptyList()
                        onSuccess(products)

                    }

                } else {
                    onError(response.errorBody()?.string() ?: "Unknown error")
                }
            }
        }
    )
}

fun getMovieDetail(
    service: ProductService,
    plot: String,
    api_key: String,
    search_query: String,
    onSuccess: (products: DetailRoot) -> Unit,
    onError: (error: String) -> Unit
) {


    service.getDetail(plot, api_key, search_query ).enqueue(
        object : Callback<DetailRoot> {
            override fun onFailure(call: Call<DetailRoot>?, t: Throwable) {
                Log.d(TAG, "Error ${t.message}")
                onError(t.message ?: "unknown error")
            }

            override fun onResponse(
                call: Call<DetailRoot>?,
                response: Response<DetailRoot>
            ) {
                Log.d(TAG, "got a response $response")
                if (response.isSuccessful) {

                    if (response.body()!!.response){
                        Log.d(TAG, "got a response ${response.body()}")

                        val products: DetailRoot = response.body()!!
                        onSuccess(products)
                    }else {

                        //onError(response.body()?.toString() ?: "Something Went wrong")
                    }

                } else {
                    onError(response.errorBody()?.string() ?: "Unknown error")
                }
            }
        }
    )
}

/**
 * Website<ecommerce built with woocommerce plugin> API communication setup via Retrofit.
 */
interface ProductService {
    /**
     * Get products from web service
     */

 @GET("/")
 fun getProducts(
     @Query("type") consumer_key: String,
     @Query("apikey") consumer_secret: String,
     @Query("page") page: Int,
     @Query("s") itemsPerPage: String
 ): Call<SearchList>

 //  http://www.omdbapi.com/?plot=full&apikey=5d81e1ce&t=Guardians
 @GET("/")
 fun getDetail(
     @Query("plot") consumer_key: String,
     @Query("apikey") consumer_secret: String,
     @Query("t") itemsPerPage: String
 ): Call<DetailRoot>

companion object {
 private const val BASE_URL = "http://www.omdbapi.com"

 fun create(): ProductService {
     val logger = HttpLoggingInterceptor()
     logger.level = Level.BASIC

     val client = OkHttpClient.Builder()
         .connectTimeout(60000, TimeUnit.SECONDS)
         .writeTimeout(120000, TimeUnit.SECONDS)
         .readTimeout(120000, TimeUnit.SECONDS)
         .retryOnConnectionFailure(true)
         .addInterceptor(logger)
         .build()

     return Retrofit.Builder()
         .baseUrl(BASE_URL)
         .client(client)
         .addConverterFactory(GsonConverterFactory.create())
         .build()
         .create(ProductService::class.java)
 }
}


}