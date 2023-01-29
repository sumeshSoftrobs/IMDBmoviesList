package com.softrobs.imdb_movies_list.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.softrobs.imdb_movies_list.data.remote.apiStateManagement.APIState
import com.softrobs.imdb_movies_list.data.remote.response.Search
import com.softrobs.imdb_movies_list.databinding.ActivityMainBinding
import com.softrobs.imdb_movies_list.ui.activities.details.MovieDetailsActivity
import com.softrobs.imdb_movies_list.ui.activities.history.HistoryActivity
import com.softrobs.imdb_movies_list.ui.adapters.MoviesListRecyclerAdapter
import com.softrobs.imdb_movies_list.ui.listeners.MovieItemClickListener
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel>()
    private val roomVm by viewModels<RoomViewModel>()
    private lateinit var binding: ActivityMainBinding
    private var movieAdapter:MoviesListRecyclerAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        setContentView(binding.root)


        initView()
        callMovieListAPI("marvel")
        setUpObserver()
    }

    private fun setUpObserver() {
        lifecycleScope.launchWhenStarted {
            with(viewModel){
                observeMovieListReceivedResponse.collect {
                    when(it){
                        is APIState.SUCCESS -> {
                            binding.loader.visibility = View.GONE
                            if(it.getResponse.Response == "false"){
                                binding.notFoundTxt.visibility = View.VISIBLE
                                binding.moviesRecycler.visibility = View.GONE
                            }else{
                                roomVm.insertMovies(it.getResponse.Search)
                                setDataToView(it.getResponse.Search)
                            }
                        }
                        is APIState.ERROR -> {
                            binding.loader.visibility = View.GONE
                        }
                        is APIState.LOADING -> {
                            binding.loader.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
    }

    private fun setDataToView(search: List<Search>) {
        movieAdapter = MoviesListRecyclerAdapter(search,object : MovieItemClickListener{
            override fun onMovieItemClicked(imdbId: String) {
                val intent = Intent(applicationContext,MovieDetailsActivity::class.java)
                intent.putExtra("imdbId",imdbId)
                intent.putExtra("apiKey",getAPIKey())
                startActivity(intent)
            }
        })
        movieAdapter?.notifyDataSetChanged()
        binding.moviesRecycler.adapter = movieAdapter

    }

    private fun callMovieListAPI(search: String) {
        viewModel.invokeMovieListApi(search,getAPIKey())
    }

    private fun initView() {
        binding.moviesRecycler.setHasFixedSize(true)
        binding.moviesRecycler.layoutManager = GridLayoutManager(this,3)

        binding.etSearch.setOnEditorActionListener(OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                callMovieListAPI(binding.etSearch.text.toString().trim())
                // Only runs if there is a view that is currently focused
                this.currentFocus?.let { view ->
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                    imm?.hideSoftInputFromWindow(view.windowToken, 0)
                }
                return@OnEditorActionListener true
            }
            false
        })

        binding.historyIcon.setOnClickListener(View.OnClickListener {
           val i = Intent(this@MainActivity,HistoryActivity::class.java)
            i.putExtra("apiKey",getAPIKey())
            startActivity(i)
        })
    }

    /**
     * A native method that is implemented by the 'imdb_movies_list' native library,
     * which is packaged with this application.
     */
    private external fun getAPIKey(): String

    companion object {
        // Used to load the 'imdb_movies_list' library on application startup.
        init {
            System.loadLibrary("imdb_movies_list")
        }
    }
}