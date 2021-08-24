package com.ihrsachin.earthquake


import android.content.Context
import androidx.loader.content.AsyncTaskLoader
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.nio.charset.Charset


class MyAsyncTaskLoader(context: Context, private val baseUrl: String?) :
    AsyncTaskLoader<List<Earthquake>>(context) {

    override fun loadInBackground(): List<Earthquake> {
        val url = createUrl(baseUrl)
        /** Perform HTTP request to the URL and receive a JSON response back */
        var jsonResponse: String? = ""
        try {
            jsonResponse = makeHttpRequest(url!!)
        } catch (e: IOException) {
            // TODO Handle the IOException
        }


        /** Extract relevant fields from the JSON response and create an {@link Earthquake} object
         * Return the {@link List<Earthquake>} object as the result for the {@link MyAsyncTaskLoader}
         */

        return extractFeatureFromJson(jsonResponse!!)
    }

    private fun createUrl(urlString: String?): URL? {
        return try {
            URL(urlString)
        } catch (e: MalformedURLException) {
            null
        }
    }

    private fun makeHttpRequest(url: URL?): String {
        var jsonResponse = ""
        var urlConnection: HttpURLConnection? = null
        var inputStream: InputStream? = null
        if (url == null) return jsonResponse
        try {
            urlConnection = url.openConnection() as HttpURLConnection
            urlConnection.readTimeout = 10000
            urlConnection.connectTimeout = 15000
            urlConnection.connect()
            if(urlConnection.responseCode != 200) return jsonResponse
            inputStream = urlConnection.inputStream
            jsonResponse = readFromStream(inputStream)
        } catch (e: IOException) {
        } finally {
            inputStream?.close()
            urlConnection!!.disconnect()
        }
        return jsonResponse
    }

    private fun readFromStream(inputStream: InputStream?): String {
        val stringBuilder = StringBuilder()
        if (inputStream != null) {
            val inputStreamReader = InputStreamReader(inputStream, Charset.forName("UTF-8"))
            val bufferedReader = BufferedReader(inputStreamReader)
            var eachLine = bufferedReader.readLine()
            while (eachLine != null) {
                stringBuilder.append(eachLine)
                eachLine = bufferedReader.readLine()
            }
        }
        return stringBuilder.toString()
    }


    private fun extractFeatureFromJson(jsonResponse: String): List<Earthquake> {
        val list = ArrayList<Earthquake>()
        if(jsonResponse != "") {
            val jsonArray = JSONObject(jsonResponse).getJSONArray("features")
            for (i in 1..jsonArray.length()) {
                val propertiesObj = jsonArray.getJSONObject(i - 1).getJSONObject("properties")
                list.add(Earthquake(propertiesObj.getDouble("mag"),
                    propertiesObj.getString("place"),
                    propertiesObj.getLong("time"),
                    propertiesObj.getString("url")))
            }
        }
        return list
    }

    override fun onStartLoading() {
        forceLoad()
    }

}


