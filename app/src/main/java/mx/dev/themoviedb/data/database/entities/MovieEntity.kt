package mx.dev.themoviedb.data.database.entities
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import mx.dev.themoviedb.data.model.MovieModel

@Entity(tableName = "movie_table")
data class MovieEntity (
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name="id") var id: Int,
    //@ColumnInfo(name="id") var id: Int = 0,
    @ColumnInfo(name="adult") var adult: Boolean,
    @ColumnInfo(name="backdrop_path") var backdropPath: String,
    //@ColumnInfo(name="genre_ids") var genre_ids: List<Int>,
    @ColumnInfo(name="original_language") var originalLanguage: String,
    @ColumnInfo(name="original_title") var originalTitle: String,
    @ColumnInfo(name="overview") var overview: String,
    @ColumnInfo(name="poster_path") var posterPath: String,
    @ColumnInfo(name="release_date") var releaseDate: String,
    @ColumnInfo(name="title") var title: String,
)

    fun MovieModel.toDatabase() = MovieEntity(adult = adult,
        backdropPath=backdropPath,
        originalLanguage = originalLanguage,
        originalTitle = originalTitle,
        overview = overview,
        posterPath = posterPath,
        releaseDate = releaseDate,
        title = title, id = id
    )

