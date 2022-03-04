package mx.dev.themoviedb.ui.viewmodel

import android.location.Location
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import kotlinx.coroutines.launch
import mx.dev.themoviedb.constants.ConstantsFirebase
import mx.dev.themoviedb.data.model.LocationModel
import mx.dev.themoviedb.data.model.MovieModel
import mx.dev.themoviedb.helpers.FirebaseHelper

class LocationViewModel : ViewModel() {
    var dbReference = FirebaseHelper.getDatabaseReference()
    private val locationList = mutableListOf<LocationModel>()
    val locationListMutable = MutableLiveData<List<LocationModel>>()

    fun onCreate() {
        viewModelScope.launch {
            Log.i("ONCREA", "asasa")
            getLocationsFirebase()
        }
    }

    fun copyListToMutableList(){
        locationListMutable.postValue(locationList)
    }

    fun removeListMutable (){
        locationListMutable.postValue(emptyList())
    }

    private fun getLocationsFirebase() {
        dbReference.child(ConstantsFirebase.dbRefLocations)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        snapshot.children.forEach {
                            var location  = it.getValue<LocationModel>()
                            locationList.add(location!!)
                            Log.i("LAT", location?.latitude.toString())
                            Log.i("LOG", location?.longitude.toString())
                        }
                        locationListMutable.postValue(locationList)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }
}