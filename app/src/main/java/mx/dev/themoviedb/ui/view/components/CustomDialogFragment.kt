package mx.dev.themoviedb.ui.view.components

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import mx.dev.themoviedb.databinding.CustomDialogFragmentBinding

class CustomDialogFragment(private var title: String, private var content: String, /*private var saveCall: () -> Unit*/) : DialogFragment() {
    private lateinit var binding: CustomDialogFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = CustomDialogFragmentBinding.inflate(inflater, container, false)
        binding.tvTitle.text = title
        binding.tvContent.text = content
        binding.btnDismiss.setOnClickListener {
            dismiss()
            //saveCall()
        }
        return binding.root
    }
}