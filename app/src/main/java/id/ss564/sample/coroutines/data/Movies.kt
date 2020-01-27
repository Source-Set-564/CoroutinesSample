package id.ss564.sample.coroutines.data

import com.google.gson.annotations.SerializedName
import id.ss564.app.mvppattern.data.model.Movie

/**
 * Created by Anwar on 1/26/2020.
 */
data class Movies(
    @SerializedName("page")
    var currentPage: Int = 1,

    @SerializedName("total_pages")
    var totalPage: Int = 1,

    @SerializedName("total_results")
    var totalResult: Int = 1,

    @SerializedName("results")
    var results: List<Movie>
)