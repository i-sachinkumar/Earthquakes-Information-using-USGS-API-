package com.ihrsachin.earthquake


import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.preference.PreferenceFragment
import android.view.View
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.loader.app.LoaderManager
import androidx.loader.content.Loader


class MainActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<List<Earthquake>> {

    private var adapter : EarthquakeAdapter? = null

    val BASE_USGS_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2014-01-01&endtime=2021-01-02&minmag=7"
    var progressBar : ProgressBar? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapter = EarthquakeAdapter(this,ArrayList<Earthquake>())
        val listView = findViewById<ListView>(R.id.listView)

        listView.adapter = adapter

        // Get a reference to the LoaderManager, in order to interact with loaders.
        // Get a reference to the LoaderManager, in order to interact with loaders.
        val loaderManager: LoaderManager = supportLoaderManager

        // Initialize the loader. Pass in the int ID constant defined above and pass in null for
        // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
        // because this activity implements the LoaderCallbacks interface).

        val cm :  ConnectivityManager = applicationContext.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val nInfo : NetworkInfo? = cm.activeNetworkInfo
        if(nInfo != null && nInfo.isAvailable && nInfo.isConnected()) {
            loaderManager.initLoader(1, null, this)
        }
        else{
            progressBar = findViewById(R.id.progressBar)
            progressBar!!.visibility = View.GONE
            val starterText = findViewById<TextView>(R.id.StarterText)
            starterText.text = getString(R.string.no_network_connection)
        }
    }


    override fun onCreateLoader(id: Int, args: Bundle?): Loader<List<Earthquake>> {
        progressBar = findViewById(R.id.progressBar)
        progressBar!!.visibility = View.VISIBLE
        return MyAsyncTaskLoader(this,BASE_USGS_URL)
    }

    override fun onLoadFinished(loader: Loader<List<Earthquake>>, data: List<Earthquake>?) {
        adapter!!.clear()
        if(data != null && data.isNotEmpty()){
            adapter!!.addAll(data)
        }
        else{
            val starterText = findViewById<TextView>(R.id.StarterText)
            starterText.text = getString(R.string.no_earthquake_found)
        }
        progressBar = findViewById(R.id.progressBar)
        progressBar!!.visibility = View.GONE
    }

    override fun onLoaderReset(loader: Loader<List<Earthquake>>) {
        adapter!!.clear()
    }

}

