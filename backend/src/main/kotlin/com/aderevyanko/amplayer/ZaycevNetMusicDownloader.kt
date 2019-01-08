package com.aderevyanko.amplayer

import de.umass.lastfm.Track
import org.htmlcleaner.HtmlCleaner
import org.htmlcleaner.TagNode
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL


class ZaycevNetMusicDownloader {
    companion object {

        fun download(track: Track) : ByteArray? {
            val pageParser = HtmlCleaner()
            pageParser.properties.isAllowHtmlInsideAttributes = true;
            pageParser.properties.isAllowMultiWordAttributes = true;
            pageParser.properties.isRecognizeUnicodeChars = true;
            pageParser.properties.isOmitComments = true;

            val url = URL( "http://zaycev.net/search.html?query_search=${track.artist.replace(' ',
                    '+')}+${track.name.replace(' ', '+')}")

            val conn = url.openConnection() as HttpURLConnection

            conn.setRequestProperty("Accept-Encoding", "identity")
            conn.setChunkedStreamingMode(1048576);
            conn.connectTimeout = 180000;
            conn.readTimeout = 180000;
            conn.addRequestProperty("User-Agent",
                    "Mozilla/5.0 (X11; U; Linux i586; en-US; rv:1.7.3) Gecko/20040924"
                            + "Epiphany/1.4.4 (Ubuntu)");

            val html = conn.getInputStream()
                    .bufferedReader()
                    .use { it.readText() }

            val rootNode = pageParser.clean(html)

            var relevantSongUrl: String? = null
            var relevantSongDurationDifference = Integer.MAX_VALUE

            val nodes = rootNode.evaluateXPath("//div/[@class='musicset-track-list__items']/div").map { it as TagNode};
            for (tagNode in nodes) {
                val duration = tagNode.getAttributeByName("data-duration").toInt()

                val durationDifference = Math.abs(duration - track.duration)

                if (durationDifference < relevantSongDurationDifference) {
                    relevantSongUrl = tagNode.getAttributeByName("data-url")
                    relevantSongDurationDifference = durationDifference

                    if (relevantSongDurationDifference == 0) {
                        break
                    }
                }
            }

            if (relevantSongUrl == null) {
                return null
            } else {
                val response = URL("http://zaycev.net/$relevantSongUrl")
                        .openStream()
                        .bufferedReader()
                        .use { it.readText() }

                val jsonObj = JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1))
                val songLink = jsonObj.getString("url")

                return URL(songLink)
                        .openStream().readBytes()
            }
        }
    }
}