package id.ss564.sample.coroutines.data

/**
 * Created by Anwar on 1/26/2020.
 */
interface Repository {

    suspend fun popular(page : Int) : State<Movies>
}