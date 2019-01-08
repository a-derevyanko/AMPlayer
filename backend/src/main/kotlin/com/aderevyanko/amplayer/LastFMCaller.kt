package com.aderevyanko.amplayer

import de.umass.lastfm.Album
import de.umass.lastfm.Artist
import de.umass.lastfm.Caller
import de.umass.lastfm.Track
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

        fun findArtists(name: String) : List<Artist> {
            return Artist.search(name, getApiKey()).toList()
        }

        fun findArtistAlbums(artist: String): List<Album> {
            return Artist.getTopAlbums(artist, getApiKey()).filter {album -> album.mbid != null }
        }

        fun getAlbumInfo(artist: String, name: String, mbid: String?) : Album {
            return Album.getInfo(artist, name, mbid, getApiKey())
        }

        fun getTrackInfo(artist: String, name: String, mbid: String?) : Track {
            return Track.getInfo(artist, (mbid ?: name), getApiKey())
        }

        private fun getApiKey(): String {
            return properties.getProperty("last_fm_api")
        }
    }
}