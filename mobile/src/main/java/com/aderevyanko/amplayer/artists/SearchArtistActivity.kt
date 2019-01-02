package com.aderevyanko.amplayer.artists;

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.SearchRecentSuggestions
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.view.Menu
import android.widget.AdapterView
import com.aderevyanko.amplayer.AsyncTaskExecutor
import com.aderevyanko.amplayer.LastFMCaller
import com.aderevyanko.amplayer.LastQueriesSuggestionProvider
import com.aderevyanko.amplayer.R
import kotlinx.android.synthetic.main.activity_search_artist.*

class SearchArtistActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState:Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_artist)

        //list.emptyView = empty

        list.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, position, id ->
            kotlin.run {
                val adapter = list.adapter as ArtistsArrayAdapter
                val artist = adapter.getItem(position)
                if (artist != null) {
                    val intent = Intent(this, ArtistActivity::class.java)
                    intent.putExtra("artist", artist.name)
                    intent.putExtra("image", artist.getImageURL(artist.availableSizes().max()))

                    startActivity(intent)
                }
            }
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        handleIntent(intent)
    }

    override fun onNewIntent(intent: Intent) {
        setIntent(intent)
        handleIntent(intent)
    }

    override fun startSearch(initialQuery: String?, selectInitialQuery: Boolean, appSearchData: Bundle?, globalSearch: Boolean) {
        super.startSearch(initialQuery, selectInitialQuery, appSearchData, globalSearch)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        if (super.onPrepareOptionsMenu(menu)) {
            for (i in 0 until menu.size()) {
                menu.getItem(i).isVisible = false
            }
            val myActionMenuItem = menu.findItem(R.id.action_search)
            myActionMenuItem.isVisible = true
            (myActionMenuItem.actionView as SearchView).apply {
                setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String): Boolean {
                        val suggestions = SearchRecentSuggestions(this@SearchArtistActivity,
                                LastQueriesSuggestionProvider.AUTHORITY, LastQueriesSuggestionProvider.MODE)
                        suggestions.saveRecentQuery(query, null)
                        return true
                    }

                    override fun onQueryTextChange(s: String): Boolean {
                        // UserFeedback.show( "SearchOnQueryTextChanged: " + s);
                        return false
                    }
                })

                val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
                setSearchableInfo(searchManager.getSearchableInfo(componentName))
            }
            return true
        } else {
            return false
        }
    }

    private fun handleIntent(intent: Intent) {
        if (Intent.ACTION_SEARCH == intent.action || Intent.ACTION_VIEW == intent.action ) {
            intent.getStringExtra(SearchManager.QUERY)?.also { query ->
                searchArtists(query)
            }
        }
    }

    private fun searchArtists(query: String) {
        val suggestions = SearchRecentSuggestions(this,
                LastQueriesSuggestionProvider.AUTHORITY, LastQueriesSuggestionProvider.MODE)
        suggestions.saveRecentQuery(query, null)
        AsyncTaskExecutor({
            LastFMCaller.findArtists(query)
        }, {
            list.adapter = ArtistsArrayAdapter(applicationContext, it)
        })
    }

}
