package mx.dev.themoviedb.ui.viewmodel

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.storage.UploadTask
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mx.dev.themoviedb.constants.ConstantsFirebase
import mx.dev.themoviedb.helpers.FirebaseHelper
import java.util.*

class ImageViewModel : ViewModel() {
    var dbReference = FirebaseHelper.getDatabaseReference()
    var stReference = FirebaseHelper.getStorageReference()
    val isUploading = MutableLiveData <Boolean>()
    val isSuccessStorage = MutableLiveData <Boolean>()
    val isSuccessDatabase = MutableLiveData <Boolean>()
    val isActive = MutableLiveData <Boolean>()

    fun onCreate(){
        viewModelScope.launch {
            isUploading.postValue(false)
        }
    }

    fun onDestroy(){
        isActive.postValue(false)
    }

    fun uploadImageStorage(fileUri: Uri, size: Int) {
        CoroutineScope(Dispatchers.IO).launch{
            if (fileUri != null) {
                isUploading.postValue(true)
                isActive.postValue(true)
                val fileName = UUID.randomUUID().toString() +".jpg"
                dbReference = FirebaseHelper.getDatabaseReference()
                stReference = FirebaseHelper.getStorageReference().child("images/$fileName")
                stReference.putFile(fileUri)
                    .addOnSuccessListener(
                        OnSuccessListener<UploadTask.TaskSnapshot> { taskSnapshot ->
                            taskSnapshot.storage.downloadUrl.addOnSuccessListener {
                                isSuccessStorage.postValue(true)
                                isUploading.postValue(false)
                                uploadUrlDatabase(it.toString(), size)
                            }
                        }).addOnFailureListener(OnFailureListener {
                        isSuccessStorage.postValue(false)
                        isUploading.postValue(false)
                    })
            }
        }
    }

    private fun uploadUrlDatabase(url: String, size: Int){
        dbReference.child(ConstantsFirebase.dbRefImages).push().child(ConstantsFirebase.dbRefUrl).setValue(url).addOnSuccessListener (
            OnSuccessListener {
                    isUploading.postValue(false)
                    isSuccessDatabase.postValue(true)

        }).addOnFailureListener(OnFailureListener {
                isUploading.postValue(false)
                isSuccessDatabase.postValue(false)

        })
    }

}