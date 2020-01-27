package id.ss564.sample.coroutines.data

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Anwar on 1/26/2020.
 */
interface MovieServices {

    @GET("movie/popular")
    suspend fun popular(@Query("page") page : Int) : Response<Movies>
}