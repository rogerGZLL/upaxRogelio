package mx.dev.themoviedb.data.network
import mx.dev.themoviedb.data.model.MovieResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface ApiService {
    @GET
    suspend fun getMoviesTopRated(@Url url: String) : Response<MovieResponse>
}