package mx.dev.themoviedb.ui.view.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import mx.dev.themoviedb.R
import mx.dev.themoviedb.databinding.FragmentStorageBinding
import mx.dev.themoviedb.ui.view.components.CustomDialogFragment
import mx.dev.themoviedb.ui.viewmodel.ImageViewModel

class FragmentStorage : Fragment() {
    private lateinit var binding: FragmentStorageBinding
    private lateinit var dialog: CustomDialogFragment
    val REQUEST_CODE = 200

    //View model
    private val imageViewModel: ImageViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStorageBinding.inflate(inflater, container, false)
        initViewModel()
        binding.btnSelectImages.setOnClickListener {
            if (imageViewModel.isUploading.value == false) {
                openGalleryForImages()
            } else {
                Toast.makeText(context?.applicationContext, getString(R.string.subida_en_proceso), Toast.LENGTH_SHORT).show()
            }
        }
        return binding.root
    }

    private fun initViewModel(){
        imageViewModel.onCreate()
        imageViewModel.isActive.observe(viewLifecycleOwner, Observer {
            if (it)
                initObservers()
        })
    }

    private fun initObservers(){
        imageViewModel.isUploading.observe(viewLifecycleOwner, Observer {
            binding.llCargando.isVisible = it
        })
        imageViewModel.isSuccessStorage.observe(viewLifecycleOwner, Observer {
            if (!it){
                showDialogResults(getString(R.string.error), getString(R.string.error_images_upload))
            }
        })
        imageViewModel.isSuccessDatabase.observe(viewLifecycleOwner, Observer {
            if (imageViewModel.isUploading.value == false){
                if (!it)
                    showDialogResults(getString(R.string.error), getString(R.string.error_images_upload))
                else
                    showDialogResults(getString(R.string.exito), getString(R.string.exito_images_upload))
            }
        })
    }

    private fun openGalleryForImages() {
        var intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE);
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
            //Multiple image
            if (data?.getClipData() != null) {
                var count = data.clipData?.itemCount
                if (count != null) {
                    for (i in 0..count - 1) {
                        var imageUri: Uri = data.clipData!!.getItemAt(i).uri
                        imageViewModel.uploadImageStorage(imageUri!!, count)
                    }
                }

            } else if (data?.getData() != null) {
                //Sigle image
                var filePath = data.data
                imageViewModel.uploadImageStorage(filePath!!, 1)
            }
        }
    }

    private fun showDialogResults(title:String, content:String){
        dialog = CustomDialogFragment(title = title, content = content)
        if (!dialog.isAdded)
        dialog.show(childFragmentManager, "TAG")
    }

    override fun onDestroy() {
        super.onDestroy()
        imageViewModel.onDestroy()
    }

}