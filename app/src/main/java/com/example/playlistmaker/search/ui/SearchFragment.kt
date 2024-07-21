package com.example.playlistmaker.search.ui

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import com.example.playlistmaker.Debounce
import com.example.playlistmaker.R
import com.example.playlistmaker.search.data.ItemClickCallback
import com.example.playlistmaker.search.data.datamodels.Track
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    private var userInputReserve = ""

    private val searchViewModel: SearchViewModel by viewModel()

    private lateinit var recyclerResultsView: RecyclerView
    private lateinit var recyclerRecentView: RecyclerView
    private lateinit var searchAdapter: SearchAdapter
    private lateinit var recentAdapter: SearchAdapter
    private lateinit var searchBarField: EditText
    private lateinit var recentSearchFrame: ConstraintLayout
    private lateinit var progressBar: ProgressBar
    private lateinit var noConnectionError: ConstraintLayout
    private lateinit var noResultsError: ConstraintLayout

    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (findNavController().previousBackStackEntry == null) {
                requireActivity().finish()}
            else{findNavController().popBackStack()}

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

        searchViewModel.state.removeObserver {
            updateUI()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val fragmentView: View = inflater.inflate(R.layout.fragment_search, container, false)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, backPressedCallback)

        val debounce = Debounce()
        val handler = Handler(Looper.getMainLooper())
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
        progressBar = fragmentView.findViewById<ProgressBar>(R.id.progressBar)
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
        recentSearchFrame.isVisible = false
        progressBar.isVisible = false
        recyclerResultsView.isVisible = false
        noConnectionError.isVisible = false
        noResultsError.isVisible = false

        when (searchViewModel.state.value) {

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
                noConnectionError.isVisible = true
            }

            else -> {}
        }


    }

    private fun recyclerSetup(fragmentView: View) {
        val itemClickCallback = object : ItemClickCallback {
            override fun onClickCallback(track: Track) {
                val bundle = Bundle()
                bundle.putParcelable(Constants.PARCELABLE_TO_PLAYER_KEY, track)

                findNavController().navigate(R.id.action_navigation_search_to_player,
                    bundle)
            }

            }

        recyclerResultsView = fragmentView.findViewById(R.id.searchResultsRecycler)
        searchAdapter =
            SearchAdapter(searchViewModel.provideTrackList(), searchViewModel, itemClickCallback)
        recyclerResultsView.adapter = searchAdapter
        recyclerResultsView.layoutManager = GridLayoutManager(requireContext(), 1)

        recyclerRecentView = fragmentView.findViewById(R.id.recentRecycler)
        recentAdapter = SearchAdapter(
            searchViewModel.provideRecentTrackList(),
            searchViewModel,
            itemClickCallback
        )
        recyclerRecentView.adapter = recentAdapter
        recyclerRecentView.layoutManager = GridLayoutManager(requireContext(), 1)
    }
}
