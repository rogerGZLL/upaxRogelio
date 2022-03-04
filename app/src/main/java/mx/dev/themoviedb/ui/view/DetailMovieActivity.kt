package mx.dev.themoviedb.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.squareup.picasso.Picasso
import mx.dev.themoviedb.constants.ConstantsApi
import mx.dev.themoviedb.data.model.MovieModel
import mx.dev.themoviedb.databinding.ActivityDetailMovieBinding
import mx.dev.themoviedb.databinding.ActivityMainBinding

class DetailMovieActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDetailMovieBinding
    private lateinit var movie : MovieModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)
        movie = intent.getSerializableExtra("MOVIE") as MovieModel
        initData()
        initToolbar()
        Log.i("MOVIE", movie.overview)
    }

    private fun initData(){
        binding.collapsingToolbarMaterial.title = movie.title
        binding.tvTitleOriginal.text = movie.originalTitle
        binding.tvOverview.text = movie.overview
        binding.tvRelaseDate.text = movie.releaseDate
        Picasso.get().load(ConstantsApi.urlBaseImage+ movie.posterPath).into(binding.ivBackground)
    }

    private fun initToolbar(){
        setSupportActionBar(binding.toolbarMaterial)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}