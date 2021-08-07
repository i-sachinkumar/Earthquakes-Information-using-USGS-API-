package com.ihrsachin.earthquake

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView



class EarthquakeAdapter(context: Context, list: List<Earthquake>) : ArrayAdapter<Earthquake>(context, 0 , list){
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val current : Earthquake? = getItem(position)
        var listViewItem = convertView

        if(listViewItem == null){
            listViewItem = LayoutInflater.from(context).inflate(R.layout.list_items,parent,false)
        }
        val magTextView : TextView = listViewItem!!.findViewById(R.id.magTextView)
        magTextView.text = current!!.getMag()

        val placeTextView : TextView = listViewItem.findViewById(R.id.placeTextView)
        placeTextView.text = current.getPlace()

        val timeTextView : TextView = listViewItem.findViewById(R.id.timeTextView)
        timeTextView.text = current.getTime()

        val dateTextView : TextView = listViewItem.findViewById(R.id.dateTextView)
        dateTextView.text = current.getDate()

        val distanceTextView : TextView = listViewItem.findViewById(R.id.distanceText)
        distanceTextView.text = current.getDistance()

        return listViewItem
    }

}