package id.ss564.sample.coroutines.data

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.ConnectException
import java.util.concurrent.TimeUnit

/**
 * Created by Anwar on 1/26/2020.
 */
class RepositoryImpl private constructor() : Repository {

    private val service: MovieServices by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .client(repoClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(MovieServices::class.java)
    }

    private fun repoClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()

        //set connect and read timeout, and ping interval
        builder.connectTimeout(1, TimeUnit.MINUTES)
        builder.readTimeout(1, TimeUnit.MINUTES)
        builder.pingInterval(30, TimeUnit.SECONDS)


        //add logger interceptor
        builder.addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.HEADERS
        })


        //add api key interceptor
        builder.addInterceptor(object : Interceptor {

            override fun intercept(chain: Interceptor.Chain): Response {
                val request = chain.request()
                val url = request.url.newBuilder()
                    .addQueryParameter("api_key", "e0ea09750f99ae533ee9b37401e54f30")
                    .build()

                return chain.proceed(request.newBuilder().url(url).build())
            }
        })

        return builder.build()
    }

    override suspend fun popular(page: Int): State<Movies> {
        return try {
            val response = service.popular(page)
            if (response.isSuccessful) {
                State.Success(response.body()!!)
            } else {
                State.Error("Response error", response.errorBody())
            }
        } catch (e: ConnectException) {
            State.Error("Connection Error", e)
        } catch (e: Throwable) {
            State.Error("Unknown Error", e)
        }

    }

    companion object {
        @Volatile
        private var INSTANCE: Repository? = null

        @JvmStatic
        fun getInstance(): Repository {
            if (INSTANCE == null) {
                synchronized(RepositoryImpl::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = RepositoryImpl()
                    }
                }
            }

            return INSTANCE!!
        }
    }
}