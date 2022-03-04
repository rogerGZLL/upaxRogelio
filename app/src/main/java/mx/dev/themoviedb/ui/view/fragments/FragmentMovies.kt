package mx.dev.themoviedb.ui.view.fragments

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import mx.dev.themoviedb.R
import mx.dev.themoviedb.data.database.entities.MovieEntity
import mx.dev.themoviedb.data.database.entities.toDatabase
import mx.dev.themoviedb.data.database.getDatabase
import mx.dev.themoviedb.data.model.toDatabase
import mx.dev.themoviedb.ui.adapters.MovieAdapter
import mx.dev.themoviedb.databinding.FragmentMoviesBinding
import mx.dev.themoviedb.helpers.CheckNetwork
import mx.dev.themoviedb.ui.viewmodel.MovieViewModel

class FragmentMovies : Fragment() {
    private lateinit var binding: FragmentMoviesBinding
    private val checkNetwork = CheckNetwork()
    private lateinit var adapter: MovieAdapter

    //View model
    private val movieViewModel: MovieViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMoviesBinding.inflate(inflater, container, false)
        movieViewModel.onCreate(context?.applicationContext!!)
        initComponents()
        initObservers()
        detectEndRecycler()
        binding.btnRecharge.setOnClickListener {
            movieViewModel.onCreate(context?.applicationContext!!)
        }
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun initComponents() {
        binding.rvMovies.layoutManager = LinearLayoutManager(context?.applicationContext)
        binding.srRefresh.setOnRefreshListener {
            movieViewModel.onCreate(context?.applicationContext!!)
            movieViewModel.isRefreshing.postValue(true)
        }
    }

    private fun detectEndRecycler() {
        binding.rvMovies.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            @RequiresApi(Build.VERSION_CODES.M)
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {
                    if (movieViewModel.isLoadingNew?.value == false) {
                        if (!movieViewModel.movieListMutable.value.isNullOrEmpty()) {
                            if (movieViewModel.movieListMutable.value?.size!! < movieViewModel.total) {
                                if (checkNetwork.isOnline(context!!.applicationContext)) {
                                    movieViewModel.callAPI(
                                        firstTime = false,
                                        context?.applicationContext!!
                                    )
                                } else {
                                    Toast.makeText(
                                        context?.applicationContext,
                                        getString(R.string.error_red_descripción),
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            } else {
                                movieViewModel.isFinalList.postValue(true)
                            }
                        }
                    }
                }
            }
        })
    }

    private fun initObservers() {
        movieViewModel.isLoading.observe(viewLifecycleOwner, Observer {
            binding.pbLoading.isVisible = it
        })
        movieViewModel.isLoadingNew.observe(viewLifecycleOwner, Observer {
            binding.pbLoadingNew.isVisible = it
        })
        movieViewModel.isFinalList.observe(viewLifecycleOwner, Observer {
            binding.tvFinalList.isVisible = it
        })
        movieViewModel.isRefreshing.observe(viewLifecycleOwner, Observer {
            binding.srRefresh.isRefreshing = false
        })
        movieViewModel.isDataLoaded.observe(viewLifecycleOwner, Observer {
            if (it) {
                binding.rvMovies.isVisible = true
                binding.llErrorNetwork.isVisible = false
            } else {
                binding.rvMovies.isVisible = false
                binding.llErrorNetwork.isVisible = true
            }
        });
        movieViewModel.movieListMutable.observe(viewLifecycleOwner, Observer {
            adapter = MovieAdapter(it)
            if (binding.rvMovies.adapter == null) {
                binding.rvMovies.adapter = adapter
            }
            adapter.notifyDataSetChanged()
            GlobalScope.launch {
                getDatabase(context?.applicationContext!!).getMovieDao()
                    .insertAllMovies(it.map { it.toDatabase() })
            }
        });
        movieViewModel.movieListEntityMutable.observe(viewLifecycleOwner, Observer {
            Log.i("LIST", it.size.toString())
            movieViewModel.movieListMutable.postValue(it.map { it.toDatabase() })
        })
    }

}