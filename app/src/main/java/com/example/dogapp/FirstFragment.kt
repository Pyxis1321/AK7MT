package com.example.dogapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.dogapp.databinding.FragmentFirstBinding
import com.example.dogapp.utils.PictureViewModelFactory

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private lateinit var viewModel: PictureViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val factory = PictureViewModelFactory(requireActivity().application)
        viewModel = ViewModelProvider(this, factory).get(PictureViewModel::class.java)
        viewModel = ViewModelProvider(this).get(PictureViewModel::class.java)

        viewModel.picture.observe(viewLifecycleOwner, Observer { picture ->
            picture.message?.let { imageUrl ->
                Glide.with(this).load(imageUrl).into(view.findViewById(R.id.imageView_dog))
            }
        })

        viewModel.getRandomDogImage()

        view.findViewById<Button>(R.id.button_get_image).setOnClickListener {
            viewModel.getRandomDogImage()
        }
        view.findViewById<Button>(R.id.button_second_fragment).setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
        view.findViewById<Button>(R.id.button_save_image).setOnClickListener {
            viewModel.saveCurrentImage()
        }
    }
}