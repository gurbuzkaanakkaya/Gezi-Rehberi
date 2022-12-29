package com.example.projectpazarama.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseOpenHelper(
    context: Context,
    name: String,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
) : SQLiteOpenHelper(context, name, factory, version) {

    companion object {
        val ID = "Id"
        val TABLE_PLACE = "Place"
        val TABLE_VISIT_HISTORY = "VisitHistory"
        val TABLE_PLACE_IMAGE = "PlaceImage"
        val PLACE_NAME = "Name"
        val PLACE_DEFINITION = "Definition"
        val PLACE_DESCRIPTION = "Description"
        val PLACE_LAST_VISIT_DATE = "LastVisitDate"
        val PLACE_PRIORITY = "Priority"
        val PLACE_LATITUDE = "Latitude"
        val PLACE_LONGITUDE = "Longitude"
        val PLACE_VISITED = "Visited"
        val VISIT_HISTORY_DATE = "Date"
        val VISIT_HISTORY_DESCRIPTION = "Description"
        val VISIT_HISTORY_PLACE_ID = "PlaceId"
        val PLACE_IMAGE_DATE = "Date"
        val PLACE_IMAGE_IMAGE = "Image"
        val PLACE_IMAGE_PLACE_ID = "PlaceId"

    }

    override fun onCreate(p0: SQLiteDatabase?) {
        val placeCreateQuery =
            "CREATE TABLE $TABLE_PLACE($ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,$PLACE_NAME TEXT,$PLACE_DEFINITION TEXT, $PLACE_DESCRIPTION TEXT, $PLACE_LAST_VISIT_DATE TEXT, $PLACE_PRIORITY INTEGER, $PLACE_LATITUDE TEXT, $PLACE_LONGITUDE TEXT, $PLACE_VISITED INTEGER)"
        val visitHistoryCreateQuery =
            "CREATE TABLE $TABLE_VISIT_HISTORY($ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,$VISIT_HISTORY_DATE TEXT, $VISIT_HISTORY_DESCRIPTION TEXT,$VISIT_HISTORY_PLACE_ID INTEGER, FOREIGN KEY($VISIT_HISTORY_PLACE_ID) REFERENCES $TABLE_PLACE($ID) ON UPDATE CASCADE ON DELETE CASCADE)"
        val placeImageCreateQuery = "CREATE TABLE $TABLE_PLACE_IMAGE($ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,$PLACE_IMAGE_DATE TEXT, $PLACE_IMAGE_IMAGE BLOB, $PLACE_IMAGE_PLACE_ID INTEGER,FOREIGN KEY($PLACE_IMAGE_PLACE_ID) REFERENCES $TABLE_PLACE($ID) ON UPDATE CASCADE ON DELETE CASCADE)"

        p0!!.execSQL(placeCreateQuery)
        p0.execSQL(visitHistoryCreateQuery)
        p0.execSQL(placeImageCreateQuery)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {

    }

}