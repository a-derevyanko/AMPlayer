package com.aderevyanko.amplayer

import android.content.SearchRecentSuggestionsProvider

class LastQueriesSuggestionProvider : SearchRecentSuggestionsProvider() {
    init {
        setupSuggestions(AUTHORITY, MODE)
    }

    companion object {
        val AUTHORITY = LastQueriesSuggestionProvider::class.java.name
        val MODE = SearchRecentSuggestionsProvider.DATABASE_MODE_QUERIES
    }
}