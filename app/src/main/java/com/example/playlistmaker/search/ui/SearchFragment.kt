package com.example.playlistmaker.search.ui

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.Constants
import com.example.playlistmaker.R
import com.example.playlistmaker.search.data.ItemClickCallback
import com.example.playlistmaker.search.domain.Track
import com.example.playlistmaker.utils.debounce
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    private var userInputReserve = ""

    private val searchViewModel: SearchViewModel by viewModel()

    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    val debounceSearch = debounce<Unit>(Constants.CLICK_DEBOUNCE_DELAY, coroutineScope, true) {
        handleSearch()
    }
    val debounceClick = debounce<Unit>(Constants.CLICK_DEBOUNCE_DELAY, coroutineScope, true) {
        searchViewModel.setClickDebounce(true)
    }

    private lateinit var recyclerResultsView: RecyclerView
    private lateinit var recyclerRecentView: RecyclerView
    private lateinit var searchAdapter: RecyclerAdapter
    private lateinit var recentAdapter: RecyclerAdapter
    private lateinit var searchBarField: EditText
    private lateinit var recentSearchFrame: ConstraintLayout
    private lateinit var progressBar: ProgressBar
    private lateinit var noConnectionError: ConstraintLayout
    private lateinit var noResultsError: ConstraintLayout

    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            findNavController().popBackStack()

            true
        }
    }


    companion object {
        fun newInstance() = SearchFragment()
        private const val USER_INPUT = "userInput"
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (isAdded) {
            outState.putString(USER_INPUT, userInputReserve)
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState != null) {
            userInputReserve = savedInstanceState.getString(USER_INPUT, "")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        backPressedCallback.remove()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val fragmentView: View = inflater.inflate(R.layout.fragment_search, container, false)

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            backPressedCallback
        )


        searchViewModel.state.observe(viewLifecycleOwner, Observer {
            updateUI()
        })
        recyclerSetup(fragmentView)

        searchViewModel.recentTrackListLiveData.observe(viewLifecycleOwner, Observer {
            recentAdapter.notifyDataSetChanged()
        })

        searchBarField = fragmentView.findViewById<EditText>(R.id.searchBarField)
        val searchBarClear = fragmentView.findViewById<ImageButton>(R.id.searchBarClear)
        val searchRefresh = fragmentView.findViewById<Button>(R.id.searchRefresh)
        progressBar = fragmentView.findViewById(R.id.progressBar)
        noConnectionError = fragmentView.findViewById<ConstraintLayout>(R.id.noConnectionError)
        noResultsError = fragmentView.findViewById<ConstraintLayout>(R.id.noResultsError)


        recentSearchFrame = fragmentView.findViewById<ConstraintLayout>(R.id.recentSearchFrame)
        val clearSearchHistory = fragmentView.findViewById<Button>(R.id.clearSearchHistory)


        if (savedInstanceState != null) {
            searchBarField.setText(userInputReserve)
        }


        searchBarField.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus && searchBarField.text.isEmpty()) {
                if (searchViewModel.isRecentListEmpty()) {
                    searchViewModel.setState(SearchState.NO_HISTORY)
                } else {
                    searchViewModel.setState(SearchState.SHOW_HISTORY)
                }
            }

        }

        searchBarField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (searchBarField.text.isNotEmpty()) {

                    debounceSearch(Unit)
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })


        clearSearchHistory.setOnClickListener {
            searchViewModel.clearRecentList()
            searchViewModel.encodeRecentTrackList()
            searchViewModel.setState(SearchState.NO_HISTORY)
        }


        searchBarField.doOnTextChanged { text, start, before, count ->
            if (text?.isNotEmpty() == true) {
                searchBarClear.isVisible = true
            } else {
                searchBarClear.visibility = View.INVISIBLE
                if (!searchViewModel.isRecentListEmpty()) {
                    searchViewModel.setState(SearchState.SHOW_HISTORY)
                } else {
                    searchViewModel.setState(SearchState.NO_HISTORY)
                }
            }
            userInputReserve = text.toString()
        }

        searchBarField.requestFocus()


        searchBarClear.setOnClickListener {
            searchBarField.text.clear()
            val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(
                searchBarField.windowToken,
                0
            )

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


        return fragmentView
    }

    private fun handleSearch() {
        if (searchBarField.text.toString() == "") {
            return
        }
        searchViewModel.setState(SearchState.LOADING)
        searchViewModel.handleSearch(searchBarField.text.toString())
        searchAdapter.notifyDataSetChanged()
    }

    private fun updateUI() {

        when (searchViewModel.state.value) {

            SearchState.LOADING -> {
                recentSearchFrame.isVisible = false
                recyclerResultsView.isVisible = false
                noConnectionError.isVisible = false
                noResultsError.isVisible = false
                progressBar.isVisible = true
            }

            SearchState.NO_RESULTS -> {
                recentSearchFrame.isVisible = false
                progressBar.isVisible = false
                recyclerResultsView.isVisible = false
                noConnectionError.isVisible = false
                noResultsError.isVisible = true
            }

            SearchState.SHOW_HISTORY -> {
                progressBar.isVisible = false
                recyclerResultsView.isVisible = false
                noConnectionError.isVisible = false
                noResultsError.isVisible = false
                recentSearchFrame.isVisible = true
            }

            SearchState.SHOW_RESULTS -> {
                recentSearchFrame.isVisible = false
                progressBar.isVisible = false
                noConnectionError.isVisible = false
                noResultsError.isVisible = false
                recyclerResultsView.isVisible = true

            }

            SearchState.NETWORK_ERROR -> {
                recentSearchFrame.isVisible = false
                progressBar.isVisible = false
                recyclerResultsView.isVisible = false
                noResultsError.isVisible = false
                noConnectionError.isVisible = true
            }


            else -> {
                progressBar.isVisible = false
                recyclerResultsView.isVisible = false
                noConnectionError.isVisible = false
                noResultsError.isVisible = false
                recentSearchFrame.isVisible = false
            }
        }


    }

    private fun recyclerSetup(fragmentView: View) {
        val itemClickCallback = object : ItemClickCallback {
            override fun onClickCallback(track: Track) {

                if (!searchViewModel.getClickDebounceState()) {
                    searchViewModel.setClickDebounce(false)
                    debounceClick(Unit)
                    val bundle = Bundle()
                    bundle.putParcelable(Constants.PARCELABLE_TO_PLAYER_KEY, track)
                    bundle.putString(Constants.SOURCE_FRAMENT_KEY, "search")
                    findNavController().navigate(
                        R.id.action_navigation_search_to_player,
                        bundle
                    )
                }
            }

        }

        recyclerResultsView = fragmentView.findViewById(R.id.searchResultsRecycler)
        searchAdapter =
            RecyclerAdapter(searchViewModel.provideTrackList(), searchViewModel, itemClickCallback)
        recyclerResultsView.adapter = searchAdapter
        recyclerResultsView.layoutManager = GridLayoutManager(requireContext(), 1)

        recyclerRecentView = fragmentView.findViewById(R.id.recentRecycler)
        recentAdapter = RecyclerAdapter(
            searchViewModel.provideRecentTrackList(),
            searchViewModel,
            itemClickCallback
        )
        recyclerRecentView.adapter = recentAdapter
        recyclerRecentView.layoutManager = GridLayoutManager(requireContext(), 1)
    }
}
