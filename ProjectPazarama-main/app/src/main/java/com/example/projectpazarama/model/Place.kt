package com.example.projectpazarama.model

import java.io.Serializable

class Place : Serializable{
    var Id : Int? = null
    var Name : String? = null
    var Definition : String? = null
    var Description : String? = null
    var LastVisitDate: String? = null
    var Priority : Int? = null
    var Latitude : String? = null
    var Longitude : String? = null
    var Visited : Boolean? = null
    var images : ArrayList<PlaceImages>? =null
}