package mx.dev.themoviedb.data.model

import com.google.gson.annotations.SerializedName
import mx.dev.themoviedb.data.database.entities.MovieEntity
import java.io.Serializable

data class MovieModel(
    @SerializedName("adult") var adult: Boolean,
    @SerializedName("backdrop_path") var backdropPath: String,
    //@SerializedName("genre_ids") var genre_ids: List<Int>,
    @SerializedName("id") var id: Int,
    @SerializedName("original_language") var originalLanguage: String,
    @SerializedName("original_title") var originalTitle: String,
    @SerializedName("overview") var overview: String,
    @SerializedName("poster_path") var posterPath: String,
    @SerializedName("release_date") var releaseDate: String,
    @SerializedName("title") var title: String,
) : Serializable

fun MovieEntity.toDatabase() = MovieModel(adult = adult,
    backdropPath=backdropPath,
    originalLanguage = originalLanguage,
    originalTitle = originalTitle,
    overview = overview,
    posterPath = posterPath,
    releaseDate = releaseDate,
    title = title, id = id
)