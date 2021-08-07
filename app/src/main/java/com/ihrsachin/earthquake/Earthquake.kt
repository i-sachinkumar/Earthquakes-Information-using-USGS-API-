package com.ihrsachin.earthquake

import java.text.SimpleDateFormat
import java.util.*

class Earthquake(private val mag: Double, private val place: String, private val time: Long) {

    public fun getMag() : String{
        return mag.toString()
    }
    public fun getPlace() : String{
        if(!place.contains("of")) return place
        return place.substringAfter("of ")
    }
    public fun getDistance() : String{
        if(!place.contains("of")) return "Near"
        return place.substringBefore("of")+" of"
    }

    /** date obj is created by passing long time in millisecond in constructor
     * formatter is created as SimpleDateFormatter in required Format
     * and then dateObj is passed to the formatter to format and return as String
     */
    private val dateObj = Date(time)
    private var formatter : SimpleDateFormat? = null
    public fun getTime(): String {
        formatter = SimpleDateFormat("HH : mm a", Locale("ENG","IND"))
        return formatter!!.format(dateObj)
    }

    public fun getDate(): String{
        formatter = SimpleDateFormat("dd MMM yyyy", Locale("ENG","IND"))
        return formatter!!.format(dateObj)
    }
}