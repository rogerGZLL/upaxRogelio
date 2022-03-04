package mx.dev.themoviedb.helpers

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

object FirebaseHelper {
    fun getStorageReference(): StorageReference {
        var firebaseStorage: FirebaseStorage = FirebaseStorage.getInstance()
        firebaseStorage.maxUploadRetryTimeMillis = 20000
        return  firebaseStorage.reference
    }

    fun getDatabaseReference(): DatabaseReference {
        return FirebaseDatabase.getInstance().reference
    }
}