package mx.dev.themoviedb.data.model

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import com.google.firebase.database.PropertyName

@IgnoreExtraProperties
data class LocationModel (
    @Exclude var uid: String,
    @PropertyName("latitude") var latitude: Double,
    @PropertyName("longitude") var longitude: Double,
) {
    constructor() : this("",0.0, 0.0)
}