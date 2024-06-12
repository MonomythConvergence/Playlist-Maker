package com.example.playlistmaker.search.ui

import android.content.Context
import android.content.SharedPreferences
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.Constants
import com.example.playlistmaker.Debounce
import com.example.playlistmaker.R
import com.example.playlistmaker.search.data.datamodels.Track
import com.example.playlistmaker.search.data.datamodels.searchServerResonse
import com.example.playlistmaker.search.data.network.RetrofitApiClient
import com.example.playlistmaker.search.domain.SearchRepositoryImpl
import com.google.gson.Gson
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale

class SearchActivity : AppCompatActivity() {

    private var userInputReserve = ""
    private val preferences: SharedPreferences by lazy {
        getSharedPreferences(Constants.RECENT_TRACKS_KEY, Context.MODE_PRIVATE)
    }
    private val apiClient: RetrofitApiClient = RetrofitApiClient()

    private val viewModel: SearchViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return SearchViewModel(SearchRepositoryImpl(preferences)) as T
            }
        }
    }

    private lateinit var recyclerResultsView: RecyclerView
    private lateinit var recyclerRecentView: RecyclerView
    private lateinit var searchAdapter: SearchAdapter
    private lateinit var recentAdapter: SearchAdapter
    private lateinit var searchBarField: EditText
    private lateinit var recentSearchFrame: LinearLayout
    private lateinit var progressBar: ProgressBar
    private lateinit var noConnectionError: LinearLayout
    private lateinit var noResultsError: LinearLayout



    companion object {
        private const val USER_INPUT = "userInput"
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(USER_INPUT, userInputReserve)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        userInputReserve = savedInstanceState.getString(USER_INPUT, "")
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.state.removeObserver(Observer {
            updateUI()
        })
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val debounce = Debounce()
        val handler = Handler(Looper.getMainLooper())
        viewModel.state.observe(this, Observer {
            updateUI()
        })

        recyclerSetup(this)

        viewModel.recentTrackListLiveData.observe(this, Observer {
            recentAdapter.notifyDataSetChanged()
        })

        searchBarField = findViewById<EditText>(R.id.searchBarField)
        val searchBarClear = findViewById<ImageButton>(R.id.searchBarClear)
        val searchRefresh = findViewById<Button>(R.id.searchRefresh)
        progressBar = findViewById<ProgressBar>(R.id.progressBar)
        noConnectionError = findViewById<LinearLayout>(R.id.noConnectionError)
        noResultsError = findViewById<LinearLayout>(R.id.noResultsError)


        recentSearchFrame = findViewById<LinearLayout>(R.id.recentSearchFrame)
        val clearSearchHistory = findViewById<Button>(R.id.clearSearchHistory)


        if (savedInstanceState != null) {
            searchBarField.setText(userInputReserve)
        }



        searchBarField.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus && searchBarField.text.isEmpty()) {
                if (viewModel.isRecentListEmpty())
                {viewModel.setState(SearchState.NO_HISTORY)}
                else {viewModel.setState(SearchState.SHOW_HISTORY)}
            }

        }




        searchBarField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (debounce.clickDebounce() && searchBarField.text.length > 1) {
                    handleSearch()
                } else {
                    handler.removeCallbacksAndMessages(null)
                    handler.postDelayed({ handleSearch() }, Debounce.CLICK_DEBOUNCE_DELAY)
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })

        clearSearchHistory.setOnClickListener {
            viewModel.clearRecentList()
            viewModel.encodeRecentTrackList()
            viewModel.setState(SearchState.NO_HISTORY)
        }

        searchBarField.doOnTextChanged { text, start, before, count ->
            if (text?.isNotEmpty() == true) {
                searchBarClear.isVisible = true
            } else {
                searchBarClear.visibility = View.INVISIBLE
                if (!viewModel.isRecentListEmpty()) {
                    viewModel.setState(SearchState.SHOW_HISTORY)
                } else {
                    viewModel.setState(SearchState.NO_HISTORY)
                }
            }
            userInputReserve = text.toString()
        }


        searchBarField.requestFocus()



        searchBarClear.setOnClickListener {
            searchBarField.text.clear()
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(searchBarField.windowToken, 0)


        }


        val backButton = findViewById<ImageButton>(R.id.backButton)

        backButton.setOnClickListener {
            finish()
        }

        searchBarField.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                handleSearch()
                true
            } else {
                false
            }


        }

        searchRefresh.setOnClickListener {
            handleSearch()
        }

    }


    fun handleSearch() {
        if (searchBarField.text.toString() == "") {
            return
        }
        viewModel.setState(SearchState.LOADING)
        val query = searchBarField.text.toString()
        val gson = Gson()
        val entity = "song"
        val call = apiClient.apiService.searchQuery(query, entity)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(
                call: Call<JsonObject>,
                response: Response<JsonObject>
            ) {
                if (response.isSuccessful) {
                    viewModel.clearTrackList()
                    if (response.code() == 200) {

                        if (response.body() != null) {
                            val parsedResponse = gson.fromJson(
                                response.body(),
                                searchServerResonse::class.java
                            )
                            val searchResults = parsedResponse?.results
                            if (searchResults != null) {

                                if (parsedResponse.resultCount == 0) {
                                    viewModel.setState(SearchState.NO_RESULTS)

                                } else {
                                    for (result in searchResults) {
                                        viewModel.addTrackToResults(

                                            Track(
                                                result.trackName,
                                                result.artistName,
                                                SimpleDateFormat(
                                                    "mm:ss",
                                                    Locale.getDefault()
                                                ).format(
                                                    result.trackTimeMillis
                                                ),
                                                result.artworkUrl100,
                                                result.trackId,
                                                result.collectionName,
                                                if (result.releaseDate != null && result.releaseDate.length >= 4) {
                                                    result.releaseDate.substring(0, 4)
                                                } else {
                                                    ""
                                                },
                                                result.primaryGenreName,
                                                result.country,
                                                result.previewUrl
                                            )
                                        )

                                    }
                                    searchAdapter.notifyDataSetChanged()
                                    viewModel.setState(SearchState.SHOW_RESULTS)
                                }
                            } else {
                                viewModel.setState(SearchState.NO_RESULTS)
                            }
                        } else {
                            viewModel.setState(SearchState.NO_RESULTS)
                        }

                    } else {
                        viewModel.setState(SearchState.NETWORK_ERROR)
                    }
                } else {
                    viewModel.setState(SearchState.NETWORK_ERROR)
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                viewModel.setState(SearchState.NETWORK_ERROR)

            }
        })

    }

    private fun updateUI() {
        recentSearchFrame.isVisible = false
        progressBar.isVisible = false
        recyclerResultsView.isVisible = false
        noConnectionError.isVisible = false
        noResultsError.isVisible = false

        when (viewModel.state.value) {

            SearchState.LOADING -> {
                progressBar.isVisible = true
            }

            SearchState.NO_RESULTS -> {
                noResultsError.isVisible = true
            }

            SearchState.SHOW_HISTORY -> {
                recentSearchFrame.isVisible = true
            }

            SearchState.SHOW_RESULTS -> {
                recyclerResultsView.isVisible = true

            }

            SearchState.NETWORK_ERROR -> {
                noConnectionError.isVisible = true}

            else -> {}
        }


    }
    private fun recyclerSetup(activity : SearchActivity) {
        recyclerResultsView = findViewById(R.id.searchResultsRecycler)
        searchAdapter = SearchAdapter(viewModel.provideTrackList(), viewModel)
        recyclerResultsView.adapter = searchAdapter
        recyclerResultsView.layoutManager = LinearLayoutManager(activity)

        recyclerRecentView = findViewById(R.id.recentRecycler)
        recentAdapter = SearchAdapter(viewModel.provideRecentTrackList(), viewModel)
        recyclerRecentView.adapter = recentAdapter
        recyclerRecentView.layoutManager = LinearLayoutManager(activity)
    }
}

