package mx.dev.themoviedb.ui.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import mx.dev.themoviedb.R
import mx.dev.themoviedb.databinding.ItemMovieBinding

class MovieViewHolder(view:View) : RecyclerView.ViewHolder(view){
    val binding = ItemMovieBinding.bind(view)
    fun bind(title: String, overview: String, urlPoster: String){
        binding.tvTitle.text = title
        binding.tvOverview.text = overview
        Picasso.get().load(urlPoster).placeholder(R.drawable.movielogo).into(binding.ivPoster)
    }
}