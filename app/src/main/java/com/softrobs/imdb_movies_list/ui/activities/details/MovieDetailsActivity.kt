package com.softrobs.imdb_movies_list.ui.activities.details

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.softrobs.imdb_movies_list.R
import com.softrobs.imdb_movies_list.data.remote.apiStateManagement.APIState
import com.softrobs.imdb_movies_list.data.remote.response.MovieDetailsModel
import com.softrobs.imdb_movies_list.databinding.ActivityMainBinding
import com.softrobs.imdb_movies_list.databinding.ActivityMovieDetailsBinding
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Error

@AndroidEntryPoint
class MovieDetailsActivity : AppCompatActivity() {
    private val viewModel by viewModels<MovieDetailsViewModel>()
    private lateinit var binding: ActivityMovieDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailsBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        setContentView(binding.root)

        val apiKey = intent.getStringExtra("apiKey")
        val imdbId = intent.getStringExtra("imdbId")
        callMovieDetailsApi(imdbId,apiKey)
        setUpObserver()


        binding.backBtn.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun setUpObserver() {
        lifecycleScope.launchWhenStarted {
            with(viewModel){
                observeMovieDetailsReceivedResponse.collect{
                    when(it){
                        is APIState.SUCCESS->{
                            updateTheUIWithData(it.getResponse)
                        }
                        is APIState.ERROR->{
                            Toast.makeText(applicationContext,"Couldn't retrieve data at the moment",
                            Toast.LENGTH_SHORT).show()
                        }
                        is APIState.LOADING->{
                            Toast.makeText(applicationContext,"Loading...",Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    private fun updateTheUIWithData(response: MovieDetailsModel) {
        val url = response.Poster
        Picasso.get().load(url).into(binding.backgroundImg)
        binding.title.text = response.Title
        binding.rating.text = response.imdbRating
        binding.movieType.text = response.Type
        binding.duration.text = response.Runtime
        binding.plot.text = response.Plot
        binding.director.text = "Director : ${response.Director}"
        binding.writer.text = "Writer : ${response.Writer}"
        binding.actors.text = "Actors : ${response.Actors}"
        val genre:List<String> = response.Genre.split(",")
        binding.genreOne.text = genre[0]
        binding.genreTwo.text = genre[genre.size-1]

    }

    private fun callMovieDetailsApi(imdbId: String?, apiKey: String?) {
        if (imdbId != null && apiKey != null) {
            viewModel.invokeMovieDetailsApi(imdbId,apiKey)
        }else{
            Toast.makeText(applicationContext,"Something went wrong..!",Toast.LENGTH_SHORT).show()
        }
    }

}