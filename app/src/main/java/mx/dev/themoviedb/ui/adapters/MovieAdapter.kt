package mx.dev.themoviedb.ui.adapters
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import mx.dev.themoviedb.R
import mx.dev.themoviedb.constants.ConstantsApi
import mx.dev.themoviedb.data.model.MovieModel
import mx.dev.themoviedb.ui.view.DetailMovieActivity
import mx.dev.themoviedb.ui.viewholders.MovieViewHolder

class MovieAdapter(private val movies : List<MovieModel>) : RecyclerView.Adapter<MovieViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return MovieViewHolder(layoutInflater.inflate(R.layout.item_movie, parent, false))
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val item = movies[position]
        val title = item.title
        val overview = item.overview
        val urlPoster = ConstantsApi.urlBaseImage+ item.posterPath
        holder.bind(title, overview, urlPoster)
        holder.binding.cvItem.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailMovieActivity::class.java)
            intent.putExtra("MOVIE", item)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = movies.size

}