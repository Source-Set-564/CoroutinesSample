package id.nz.sample.coroutines

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.snackbar.Snackbar
import id.nz.sample.coroutines.data.Repository
import id.nz.sample.coroutines.data.RepositoryImpl
import kotlinx.android.synthetic.main.activity_main.*
import sourceset564.samples.mvvm.feature.imdbmovie.ui.MovieMarginDecorator


class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MoviesViewModel
    private val movieAdapter by lazy {
        MovieAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        viewModel = ViewModelProvider(this, MovieVMFactory(RepositoryImpl.getInstance()))
            .get(MoviesViewModel::class.java)

        val layoutManager = GridLayoutManager(this, 2)
        rvMovies.layoutManager = layoutManager

        rvMovies.addItemDecoration(
            MovieMarginDecorator(margin = resources.getDimensionPixelSize(R.dimen.item_margin))
        )

        rvMovies.adapter = movieAdapter
        rvMovies.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val offset = layoutManager.spanCount * 2

                val lastVisible = layoutManager.findLastVisibleItemPosition()
                val itemCount = layoutManager.itemCount
                val canNext =
                    itemCount > 0 && itemCount <= lastVisible + offset

                if (canNext) {
                    viewModel.nextPage()
                }
            }
        })

        swipeRefres.setOnRefreshListener { viewModel.refresh() }

        doObserver()
    }

    private fun doObserver() {
        viewModel.isLoading.observe(this, Observer {
            swipeRefres.isRefreshing = it
        })

        viewModel.errorMessage.observe(this, Observer {
            val snackbar = Snackbar.make(swipeRefres, it, Snackbar.LENGTH_INDEFINITE)
            snackbar.setAction("Dismiss") {
                snackbar.dismiss()
            }.show()
        })

        viewModel.movies.observe(this, Observer { movies ->
            if (viewModel.page.value?.let { it >= 2 } == true) {
                movieAdapter.add(movies)
            } else {
                movieAdapter.updateAdapter(movies)
            }
        })
    }

    override fun onPause() {
        super.onPause()
        viewModel.pause()
    }

    override fun onStop() {
        super.onStop()
        viewModel.pause()
    }

    inner class MovieVMFactory(val repository: Repository) : ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return modelClass.getConstructor(Repository::class.java)
                .newInstance(repository)
        }
    }
}
