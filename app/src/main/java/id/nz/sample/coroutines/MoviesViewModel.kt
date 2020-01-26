package id.nz.sample.coroutines

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.nz.app.mvppattern.data.model.Movie
import id.nz.sample.coroutines.data.Repository
import id.nz.sample.coroutines.data.State
import kotlinx.coroutines.*
import java.lang.Exception
import kotlin.coroutines.CoroutineContext

/**
 * Created by Anwar on 1/26/2020.
 */
class MoviesViewModel constructor(val repository: Repository) : ViewModel(), CoroutineScope {

    private val _page = MutableLiveData<Int>()
    val page: LiveData<Int>
        get() = _page

    private val _lastPage = MutableLiveData<Int>()
    val lastPage: LiveData<Int>
        get() = _lastPage

    private val _movies = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>>
        get() = _movies

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    private val _loading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _loading

    private val supervisorJob = SupervisorJob()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + supervisorJob

    init {
        _page.value = 1
        _lastPage.value = 1
        _loading.value = true
        loadMovies()
    }

    private fun loadMovies() {
        launch(Dispatchers.IO) {
            val state = repository.popular(_page.value!!)
            withContext(Dispatchers.Main) {
                _loading.value = false

                when (state) {
                    is State.Success -> {
                        _page.value = state.data.currentPage
                        _lastPage.value = state.data.totalPage
                        _movies.value = state.data.results
                    }
                    is State.Error<*> -> {
                        state.error?.run {
                            if (this is Throwable) {
                                message?.also { _errorMessage.value = it }
                            }
                        } ?: run { _errorMessage.value = state.message }
                    }
                }
            }
        }
    }

    fun refresh() {
        _page.value = 1
        _lastPage.value = 1
        _loading.value = true
        loadMovies()
    }

    fun nextPage() {
        if (_loading.value != true && _page.value != _lastPage.value) {
            _page.value = _page.value?.plus(1)
            _loading.value = true
            loadMovies()
        }
    }

    fun pause() {
        if (isActive && !supervisorJob.isCancelled) {
            supervisorJob.children.map {
                it.cancel()
            }
        }
    }
}