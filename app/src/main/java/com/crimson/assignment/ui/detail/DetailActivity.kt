package com.crimson.assignment.ui.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.crimson.assignment.Injection

import com.crimson.assignment.utils.checkInternetAccess
import com.crimson.assignment.R
import com.crimson.assignment.databinding.ActivityDetailBinding
import com.google.android.material.snackbar.Snackbar


class DetailActivity: AppCompatActivity() {
    private lateinit var _binding: ActivityDetailBinding
    private val binding get() = _binding
    private lateinit var viewModel: DetailViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val Tittle      = intent.extras["TITTLE_KEY"]
        val POSTERKEY   = intent.extras["POSTER_KEY"]
        val TYPEKEY     = intent.extras["TYPE_KEY"]
        val YEARKEY     = intent.extras["YEAR_KEY"]
        val IMDBIDKEY   = intent.extras["IMDBID_KEY"]

        SEARCH = Tittle.toString()
        // get the view model
        viewModel = ViewModelProvider(this, Injection.provideDetailViewModelFactory(this)).get(
            DetailViewModel::class.java
        )
        if (!checkInternetAccess()) {
            setLocallyvalues(Tittle.toString(),POSTERKEY.toString(),TYPEKEY.toString(),YEARKEY.toString(),IMDBIDKEY.toString())
        }


        viewModel.networkErrors.observe(this, Observer {
            Toast.makeText(this, "network issue", Toast.LENGTH_SHORT).show()
            setLocallyvalues(Tittle.toString(),POSTERKEY.toString(),TYPEKEY.toString(),YEARKEY.toString(),IMDBIDKEY.toString())
            val snack = Snackbar.make(window.decorView,"Somthing went wroung",Snackbar.LENGTH_LONG)
            snack.show()
        })

        viewModel.movieDetail.observe(this, Observer {

            if (it.isEmpty()) return@Observer

            val dataMovie = it?.get(0)
            val posterPath = dataMovie?.poster


            binding.textTitle.text = dataMovie?.title
            binding.textReleaseDate.text = dataMovie?.released
            binding.textLanguage.text = dataMovie?.language
            binding.textVote.text = dataMovie?.rated
            binding.textGenrse.text = dataMovie?.genre
            binding.textYear.text  = dataMovie?.year.toString()
            binding.textPlot.text  = dataMovie?.plot.toString()

            setPosterImage(posterPath.toString())
        })
    }

    fun setLocallyvalues(Tittle: String,POSTERKEY: String,TYPEKEY:String,YEARKEY:String,IMDBIDKEY:String){
        binding.textTitle.text = Tittle
        binding.textYear.text  = YEARKEY
        binding.textVote.text =  IMDBIDKEY
        binding.textPlot.text  = TYPEKEY
        setPosterImage(POSTERKEY)
    }

   fun setPosterImage(posterPath: String){
       /**
        * Using Glide to handle image loading.
        *
        */
       Glide.with(this)
           .load(posterPath)
           .placeholder(R.drawable.image_loading)
           .diskCacheStrategy(DiskCacheStrategy.ALL) // cache both original & resized image
           .centerCrop()
           .transition(DrawableTransitionOptions().crossFade())
           .into(binding.moviePoster)

       Glide.with(this)
           .load(posterPath)
           .placeholder(R.drawable.image_loading)
           .diskCacheStrategy(DiskCacheStrategy.ALL) // cache both original & resized image
           .centerCrop()
           .transition(DrawableTransitionOptions().crossFade())
           .into(binding.imagePoster)
   }
    companion object {
        var PLOT =  "full"
        var apikey = "5d81e1ce"
        var SEARCH = "guardians"

        fun newIntent(context: Context): Intent {
            val intent = Intent(context, DetailActivity::class.java)

            return intent
        }
    }

}