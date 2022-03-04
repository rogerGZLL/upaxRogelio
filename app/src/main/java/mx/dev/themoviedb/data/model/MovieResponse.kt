package mx.dev.themoviedb.data.model

import com.google.gson.annotations.SerializedName

data class MovieResponse(
    @SerializedName("page") var code: Int,
    @SerializedName("total_pages") var totalPages: Int,
    @SerializedName("total_results") var totalResults: Int,
    @SerializedName("results") var results: List<MovieModel>,
) {
}