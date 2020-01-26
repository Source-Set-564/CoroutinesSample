package id.nz.sample.coroutines

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import id.nz.app.mvppattern.data.model.Movie
import kotlinx.android.synthetic.main.item_movie.view.*

/**
 * Created by Anwar on 1/26/2020.
 */

class MovieAdapter : RecyclerView.Adapter<MovieAdapter.ItemViewHolder>() {

    private val items: MutableList<Movie> = mutableListOf()

    fun updateAdapter(list: List<Movie>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    fun add(list: List<Movie>) {
        items.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return LayoutInflater.from(parent.context)
            .inflate(R.layout.item_movie, parent, false)
            .let {
                ItemViewHolder(it)
            }
    }

    fun clear() = items.clear()

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val movie = items[position]
        holder.itemView.run {
            tvTitle.text = movie.title
            movie.image?.run {
                val errorDrawable =
                    ResourcesCompat.getDrawable(resources, R.drawable.image_not_found, null)

                Glide.with(ivThumb)
                    .load(IMAGE_URL + this)
                    .centerCrop()
                    .error(errorDrawable)
                    .into(ivThumb)
            } ?: ivThumb.setImageResource(R.drawable.image_not_found)
        }
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    companion object {
        const val IMAGE_URL = "http://image.tmdb.org/t/p/w185"
    }
}