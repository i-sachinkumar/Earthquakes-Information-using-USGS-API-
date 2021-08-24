package com.ihrsachin.earthquake


import android.os.AsyncTask
import android.util.Log
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.nio.charset.Charset

/** AsyncTask is deprecated so i'll be not using this in final
 * but have made for reference
 */

class QuakeAsynkTask() : AsyncTask<URL, Void, List<Earthquake>>() {
    val LOG_TAG = MainActivity::class.java.simpleName
    private var list : List<Earthquake> = ArrayList<Earthquake>()
        get() {
           return list
        }
    val BASE_USGS_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2014-01-01&endtime=2021-01-02&minmag=6"
    override fun doInBackground(vararg url: URL?): List<Earthquake> {
         val url = createUrl(BASE_USGS_URL)

        // Perform HTTP request to the URL and receive a JSON response back
        var jsonResponse: String? = ""
        try {
            jsonResponse = makeHttpRequest(url!!)
        } catch (e: IOException) {
            // TODO Handle the IOException
        }

        // Extract relevant fields from the JSON response and create an {@link Event} object

        // Extract relevant fields from the JSON response and create an {@link Event} object

        // Return the {@link Event} object as the result fo the {@link TsunamiAsyncTask}

        // Return the {@link Event} object as the result fo the {@link TsunamiAsyncTask}
        return extractFeatureFromJson(jsonResponse)
    }

    override fun onPostExecute(result: List<Earthquake>?) {
        if (result!!.isEmpty()) {
            return
        }
        list = result
    }

    fun createUrl(stringUrl : String) : URL? {
        val  url : URL
        try{
            url = URL(stringUrl)
        } catch (e : MalformedURLException){
            Log.e(LOG_TAG, "Error with creating URL", e)
            return null
        }
        return url
    }

    fun makeHttpRequest(url : URL) : String{
        var jsonResponse = ""
        var urlConnection : HttpURLConnection? = null
        var inputStream : InputStream? = null
        try{
            urlConnection = url.openConnection() as HttpURLConnection?
            urlConnection!!.requestMethod = "GET"
            urlConnection.readTimeout = 10000
            urlConnection.connectTimeout = 15000
            urlConnection.connect()
            inputStream = urlConnection.inputStream
            jsonResponse = readFromStream(inputStream)            //read().toString()
        } catch (e : IOException){

        } finally {
            urlConnection?.disconnect()
            inputStream?.close()
        }
        return jsonResponse
    }


    @Throws(IOException::class)
    fun readFromStream(inputStream: InputStream?) : String {
        val stringBuilder : StringBuilder = StringBuilder()
        if(inputStream != null){
            val inputStreamReader : InputStreamReader = InputStreamReader(inputStream, Charset.forName("UTF-8"))
            val bufferedReader = BufferedReader(inputStreamReader)
            var line : String? = bufferedReader.readLine()
            while(line != null){
                stringBuilder.append(line)
                line = bufferedReader.readLine()
            }
        }
        return stringBuilder.toString()
    }

    private fun extractFeatureFromJson(jsonResponse: String?): List<Earthquake> {
        val listOfQuake = ArrayList<Earthquake>()
        if(jsonResponse != null) {
            val root = JSONObject(jsonResponse)
            val jsonArray = root.getJSONArray("features")
            for (i in 1..jsonArray.length()) {
                val currObj = jsonArray.getJSONObject(i-1).getJSONObject("properties")
                val magnitude = currObj.getDouble("mag")
                val place = currObj.getString("place")
                val time = currObj.getLong("time")
                listOfQuake.add(Earthquake(magnitude,place,time,currObj.getString("url")))
            }
        }
        return listOfQuake
    }
}