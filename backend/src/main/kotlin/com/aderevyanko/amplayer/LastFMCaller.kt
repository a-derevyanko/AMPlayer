package com.aderevyanko.amplayer

import de.umass.lastfm.Album
import de.umass.lastfm.Artist
import de.umass.lastfm.Caller
import de.umass.lastfm.cache.MemoryCache
import java.util.*

class LastFMCaller {
    companion object {
        var properties = Properties();

        init {
            Thread.currentThread().contextClassLoader.getResourceAsStream("lastfm.properties")
                    .use { properties.load(it) }
            Caller.getInstance().cache = MemoryCache() //todo
        }

        fun main(args: Array<String>) {
            println("Hello, world!")
        }

        fun findArtists(name: String) : List<Artist> {
            return Artist.search(name, getApiKey()).toList()
        }

        fun findArtistAlbums(artist: String) : List<Album> {
            return Artist.getTopAlbums(artist, getApiKey()).toList()
        }

        private fun getApiKey(): String {
            return properties.getProperty("last_fm_api")
        }
    }
}