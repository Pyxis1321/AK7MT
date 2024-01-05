package com.example.dogapp

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.dogapp.databinding.FragmentSecondBinding
import com.example.dogapp.utils.PictureViewModelFactory

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Used for accessing the fragmented views
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root

    }

    private lateinit var viewModel: PictureViewModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val factory = PictureViewModelFactory(requireActivity().application)
        viewModel = ViewModelProvider(this, factory).get(PictureViewModel::class.java)

        //Retrieves DogAppPrefs file for persistent storage
        val sharedPref = activity?.getSharedPreferences("DogAppPrefs", Context.MODE_PRIVATE)
        val savedImages = sharedPref?.getStringSet("savedImages", setOf())?.toMutableSet() ?: mutableSetOf()

        // Dynamically render each image with set styling
        savedImages.forEach { imageUrl ->
            val imageLayout = LinearLayout(context).apply {
                orientation = LinearLayout.VERTICAL
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
            }

            val imageView = ImageView(context).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(0, 20, 0, 20) // top and bottom margins
                }
                scaleType = ImageView.ScaleType.FIT_CENTER // Maintains aspect ratio, fits within the bounds
                Glide.with(this@SecondFragment)
                    .load(imageUrl)
                    .into(this)
            }

            val buttonLayoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = Gravity.CENTER_HORIZONTAL
            }

            val deleteButton = Button(context).apply {
                text = "Delete"
                textSize = 18f
                setTextColor(Color.WHITE)
                background = ContextCompat.getDrawable(context, R.drawable.rounded_button)
                layoutParams = buttonLayoutParams
                setOnClickListener {
                    viewModel.deleteImage(imageUrl)
                    imageLayout.visibility = View.GONE
                }
            }

            imageLayout.addView(imageView)
            imageLayout.addView(deleteButton)
            binding.imagesContainer.addView(imageLayout)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}