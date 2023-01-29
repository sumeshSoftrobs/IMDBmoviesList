package com.softrobs.imdb_movies_list.ui.activities.history

import android.content.Intent
import android.graphics.ColorSpace.Model
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.softrobs.imdb_movies_list.data.remote.response.Search
import com.softrobs.imdb_movies_list.databinding.ActivityHistoryBinding
import com.softrobs.imdb_movies_list.ui.activities.RoomViewModel
import com.softrobs.imdb_movies_list.ui.activities.details.MovieDetailsActivity
import com.softrobs.imdb_movies_list.ui.adapters.MoviesListRecyclerAdapter
import com.softrobs.imdb_movies_list.ui.listeners.MovieItemClickListener
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HistoryActivity : AppCompatActivity() {
    private val roomVm by viewModels<RoomViewModel>()
    private lateinit var binding: ActivityHistoryBinding
    private lateinit var mList: List<Search>
    private lateinit var mAdapter:MoviesListRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        setContentView(binding.root)

        initView()
    }

    private fun initView() {
        binding.moviesRecycler.setHasFixedSize(true)
        binding.moviesRecycler.layoutManager = GridLayoutManager(this,3)


        with(roomVm){
            getAllMovies()
            binding.loader.visibility = View.VISIBLE
            setUpObserver()
        }
        binding.iconClose.setOnClickListener(View.OnClickListener {
            finish()
        })
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                filter(s.toString())
            }
        })
    }
    private fun filter(text: String) {
        val newList: ArrayList<Search> = ArrayList()
        for (item:Search in mList){
            if (item.Title.lowercase().contains(text.lowercase())){
                newList.add(item)
            }
        }

        mAdapter.setFilter(newList)
    }
    private fun setUpObserver() {
        lifecycleScope.launchWhenStarted {
            with(roomVm){
                roomDatabase.observe(this@HistoryActivity, Observer {
                    mList = it as List<Search>
                    mAdapter = MoviesListRecyclerAdapter(mList,object : MovieItemClickListener{
                            override fun onMovieItemClicked(imdbId: String) {
                                val i = Intent(this@HistoryActivity,MovieDetailsActivity::class.java)
                                i.putExtra("imdbId",imdbId)
                                i.putExtra("apiKey",intent.getStringExtra("apiKey"))
                                startActivity(i)
                            }
                        })
                    binding.moviesRecycler.adapter = mAdapter
                    binding.loader.visibility = View.GONE

                })
            }
        }
    }
}