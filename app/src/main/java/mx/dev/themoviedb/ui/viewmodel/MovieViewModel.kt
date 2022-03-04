package mx.dev.themoviedb.ui.viewmodel

import android.app.Application
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import mx.dev.themoviedb.constants.ConstantsApi
import mx.dev.themoviedb.data.database.entities.MovieEntity
import mx.dev.themoviedb.data.database.entities.toDatabase
import mx.dev.themoviedb.data.database.getDatabase
import mx.dev.themoviedb.data.model.MovieModel
import mx.dev.themoviedb.data.model.MovieResponse
import mx.dev.themoviedb.data.model.toDatabase
import mx.dev.themoviedb.data.network.ApiService
import mx.dev.themoviedb.helpers.CheckNetwork
import mx.dev.themoviedb.helpers.RetrofitHelper

class MovieViewModel(): ViewModel() {
    private val retrofit = RetrofitHelper.getRetrofit()
    private val movieListModel = mutableListOf<MovieModel>()
    val movieListMutable = MutableLiveData<List<MovieModel>>()
    private val movieListEntity = mutableListOf<MovieEntity>()
    val movieListEntityMutable = MutableLiveData<List<MovieEntity>>()
    private var page: Int = 1
    var total: Int = 0
    val isLoading = MutableLiveData <Boolean>()
    val isLoadingNew = MutableLiveData <Boolean>()
    val isDataLoaded = MutableLiveData <Boolean>()
    val isFinalList = MutableLiveData <Boolean>()
    val isRefreshing = MutableLiveData <Boolean>()
    private val checkNetwork = CheckNetwork()

    @RequiresApi(Build.VERSION_CODES.M)
    fun onCreate(context: Context){
        viewModelScope.launch {
            isDataLoaded.postValue(true)
            if (checkNetwork.isOnline(context?.applicationContext!!)){
                callAPI(true, context)
            } else {
                showDataRoom(context)
            }
        }
    }

    private fun showDataRoom(context: Context){
        viewModelScope.launch {
            movieListEntity.addAll(getDatabase(context).getMovieDao().getAllMovies())
            if (movieListEntity.isNotEmpty()){
                movieListEntityMutable.postValue(movieListEntity)
                isLoading.postValue(false)
                isLoadingNew.postValue(false)
            } else {
                showErrorNetwork()
            }
        }
    }

    private fun showErrorNetwork(){
        isDataLoaded.postValue(false)
    }

    fun callAPI(firstTime: Boolean, context: Context){
        isFinalList.postValue(false)
        if (firstTime){
            isLoading.postValue(true)
            isLoadingNew.postValue(false)
        } else {
            isLoading.postValue(false)
            isLoadingNew.postValue(true)
        }
        CoroutineScope(Dispatchers.IO).launch {
            var urlApi = "${ConstantsApi.movieToprated}?api_key=${ConstantsApi.apiKey}&page=${page}&languaje=${ConstantsApi.apiLanguageEn}"
            val call = retrofit.create(ApiService::class.java).getMoviesTopRated(urlApi)
            if (call.isSuccessful){
                val movieResponse: MovieResponse? = call.body()
                val movie = movieResponse?.results ?: emptyList()
                movieListModel.addAll(movie)
                Log.i("RES", movieListModel.size.toString())
                movieListMutable.postValue(movieListModel)
                isLoading.postValue(false)
                isLoadingNew.postValue(false)
                if (firstTime) isDataLoaded.postValue(true)
                page += 1
                if (firstTime) total = movieResponse?.totalPages ?: 10
                isRefreshing.postValue(false)
            } else {
                isLoading.postValue(false)
                isLoadingNew.postValue(false)
                if (firstTime) isDataLoaded.postValue(false)
                isRefreshing.postValue(false)
            }
        }
    }
}