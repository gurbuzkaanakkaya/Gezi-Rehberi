package com.example.projectpazarama.data

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.example.projectpazarama.Util.DateHelper
import com.example.projectpazarama.Util.ImageHelper
import com.example.projectpazarama.model.Place
import com.example.projectpazarama.model.PlaceImages
import com.example.projectpazarama.model.VisitHistory

class TravelOperations(context: Context) {
    var sqLiteDatabase: SQLiteDatabase? = null
    var dbOpenHelper: DatabaseOpenHelper
    init {
        dbOpenHelper = DatabaseOpenHelper(context, "TravelDB", null, 1)
    }
    fun open() {
        sqLiteDatabase = dbOpenHelper.writableDatabase
    }
    fun close() {
        if (sqLiteDatabase != null && sqLiteDatabase!!.isOpen) {
            sqLiteDatabase!!.close()
        }
    }
    fun addPlace(place : Place,images:ArrayList<PlaceImages>){
        val contentValues = ContentValues()
        contentValues.put(DatabaseOpenHelper.PLACE_NAME,place.Name)
        contentValues.put(DatabaseOpenHelper.PLACE_DEFINITION, place.Definition)
        contentValues.put(DatabaseOpenHelper.PLACE_DESCRIPTION,place.Description)
        contentValues.put(DatabaseOpenHelper.PLACE_LAST_VISIT_DATE,place.LastVisitDate)
        contentValues.put(DatabaseOpenHelper.PLACE_PRIORITY,place.Priority)
        contentValues.put(DatabaseOpenHelper.PLACE_LATITUDE,place.Latitude)
        contentValues.put(DatabaseOpenHelper.PLACE_LONGITUDE,place.Longitude)
        contentValues.put(DatabaseOpenHelper.PLACE_VISITED,0)
        open()
        val placeId = sqLiteDatabase!!.insert(DatabaseOpenHelper.TABLE_PLACE, null, contentValues)
        close()
        if (images.isNotEmpty()){
            images.forEach {
                it.PlaceId = placeId.toInt()
                addPlaceImage(it)
            }
        }
    }
    fun updatePlace(place:Place){
        val contentValues = ContentValues()
        contentValues.put(DatabaseOpenHelper.PLACE_NAME, place.Name)
        contentValues.put(DatabaseOpenHelper.PLACE_DEFINITION, place.Definition)
        contentValues.put(DatabaseOpenHelper.PLACE_DESCRIPTION, place.Description)
        contentValues.put(DatabaseOpenHelper.PLACE_LAST_VISIT_DATE, place.LastVisitDate)
        contentValues.put(DatabaseOpenHelper.PLACE_PRIORITY, place.Priority)
        contentValues.put(DatabaseOpenHelper.PLACE_LATITUDE, place.Latitude)
        contentValues.put(DatabaseOpenHelper.PLACE_LONGITUDE, place.Longitude)
        contentValues.put(DatabaseOpenHelper.PLACE_VISITED, place.Visited)
        open()
        sqLiteDatabase!!.update(
            DatabaseOpenHelper.TABLE_PLACE, contentValues, "${DatabaseOpenHelper.ID} = ?",
            arrayOf(place.Id.toString()))
        close()
    }

    private fun changeVisitState(placeId: Int){
        val contentValues = ContentValues()
        contentValues.put(DatabaseOpenHelper.PLACE_VISITED, 1)
        open()
        sqLiteDatabase!!.update(
            DatabaseOpenHelper.TABLE_PLACE, contentValues, "${DatabaseOpenHelper.ID} = ?", arrayOf(placeId.toString()))
        close()

    }
    fun deletePlace(deleteId: Int) {
        open()
        sqLiteDatabase!!.delete(
            DatabaseOpenHelper.TABLE_PLACE, "${DatabaseOpenHelper.ID} = ?",
            arrayOf(deleteId.toString()))
        close()
    }



    private fun checkVisitedValue(getVisited: Int) : Boolean{
        return (getVisited ==1)
    }
    private fun selectVisitedPlaces(): Cursor{
        val selectAllQuery = "Select * from ${DatabaseOpenHelper.TABLE_PLACE} where ${DatabaseOpenHelper.PLACE_VISITED} = 1 ORDER BY ${DatabaseOpenHelper.PLACE_LAST_VISIT_DATE}"
        val cursor =  sqLiteDatabase!!.rawQuery(selectAllQuery, null)
        return cursor
    }

    private fun selectUnvisitedPlaces(): Cursor{

        val selectAllQuery = "Select * from ${DatabaseOpenHelper.TABLE_PLACE} where ${DatabaseOpenHelper.PLACE_VISITED} = 0 ORDER BY ${DatabaseOpenHelper.PLACE_PRIORITY}"
        val cursor = sqLiteDatabase!!.rawQuery(selectAllQuery, null)
        return cursor
    }

    private fun selectPlaceById(placeId: Int): Cursor{

        val selecById = "Select * from ${DatabaseOpenHelper.TABLE_PLACE} where ${DatabaseOpenHelper.ID} = $placeId"
        val cursor = sqLiteDatabase!!.rawQuery(selecById, null)
        return cursor
    }

    fun getPlaceById(placeId: Int):Place{
        open()
        val selectedPlace = getPlace(selectPlaceById(placeId))
        close()
        return selectedPlace[0]
    }




    fun getVisitedPlaces(): ArrayList<Place>{
        open()
        val visitedPlaces = getPlace(selectVisitedPlaces())
        close()
        return visitedPlaces
    }
    fun getUnVisitedPlaces(): ArrayList<Place>{
        open()
        val visitedPlaces = getPlace(selectUnvisitedPlaces())
        close()
        return visitedPlaces
    }

    @SuppressLint("Range")
    private fun getPlace(selectionCursor : Cursor): ArrayList<Place> {
        val places = ArrayList<Place>()
        open()
        val cursor = selectionCursor
        if (cursor.moveToFirst()) {
            do {
                val place = Place()
                val placeId = cursor.getInt(0)
                place.Id = placeId
                place.Name = cursor.getString(cursor.getColumnIndex(DatabaseOpenHelper.PLACE_NAME))
                place.Definition = cursor.getString(cursor.getColumnIndex(DatabaseOpenHelper.PLACE_DEFINITION))
                place.Description = cursor.getString(cursor.getColumnIndex(DatabaseOpenHelper.PLACE_DESCRIPTION))
                place.LastVisitDate = cursor.getString(cursor.getColumnIndex(DatabaseOpenHelper.PLACE_LAST_VISIT_DATE))
                place.Priority = cursor.getInt(cursor.getColumnIndex(DatabaseOpenHelper.PLACE_PRIORITY))
                place.Latitude = cursor.getString(cursor.getColumnIndex(DatabaseOpenHelper.PLACE_LATITUDE))
                place.Longitude = cursor.getString(cursor.getColumnIndex(DatabaseOpenHelper.PLACE_LONGITUDE))
                place.Visited = checkVisitedValue(cursor.getInt(cursor.getColumnIndex(DatabaseOpenHelper.PLACE_VISITED)))
                place.images = getPlaceImagesById(placeId)
                places.add(place)
            } while (cursor.moveToNext())
        }
        close()
        return places
    }
    fun addPlaceImage(placeImages : PlaceImages){
        val contentValues = ContentValues()
        if (placeImages.Image != null){
            contentValues.put(DatabaseOpenHelper.PLACE_IMAGE_IMAGE,ImageHelper.bitmapToBlob(placeImages.Image!!))
        }
        contentValues.put(DatabaseOpenHelper.PLACE_IMAGE_DATE,DateHelper.toDbDateFormat(placeImages.Date!!))
        contentValues.put(DatabaseOpenHelper.PLACE_IMAGE_PLACE_ID,placeImages.PlaceId)
        open()
        sqLiteDatabase!!.insert(DatabaseOpenHelper.TABLE_PLACE_IMAGE, null, contentValues)
        close()
    }

    fun deletePlaceImage(deleteId: Int) {
        open()
        sqLiteDatabase!!.delete(
            DatabaseOpenHelper.TABLE_PLACE_IMAGE, "${DatabaseOpenHelper.ID} = ?",
            arrayOf(deleteId.toString()))
        close()
    }
    private fun selectImagesById(placeId : Int): Cursor {
        val selectAllQuery = "Select * from ${DatabaseOpenHelper.TABLE_PLACE_IMAGE} WHERE ${DatabaseOpenHelper.PLACE_IMAGE_PLACE_ID} = $placeId"
        return sqLiteDatabase!!.rawQuery(selectAllQuery, null)
    }

    @SuppressLint("Range")
    fun getPlaceImagesById(placeId:Int): ArrayList<PlaceImages> {
        val placeImages = ArrayList<PlaceImages>()
        open()
        val cursor = selectImagesById(placeId)
        if (cursor.moveToFirst()) {
            do {
                val placeImage= PlaceImages()
                placeImage.Id = cursor.getInt(0)
                placeImage.Image = ImageHelper.blobToBitmap(cursor.getBlob(cursor.getColumnIndex(DatabaseOpenHelper.PLACE_IMAGE_IMAGE)))
                placeImage.Date = DateHelper.fromDbDateFormat(cursor.getString(cursor.getColumnIndex(DatabaseOpenHelper.PLACE_IMAGE_DATE)))
                placeImage.PlaceId = cursor.getInt(cursor.getColumnIndex(DatabaseOpenHelper.PLACE_IMAGE_PLACE_ID))
                placeImages.add(placeImage)
            } while (cursor.moveToNext())
        }
        close()
        return placeImages
    }
    fun addVisitHistory(visitHistory: VisitHistory){
        val contentValues = ContentValues()
        contentValues.put(DatabaseOpenHelper.VISIT_HISTORY_DATE,visitHistory.Date)
        contentValues.put(DatabaseOpenHelper.VISIT_HISTORY_DESCRIPTION,visitHistory.Description)
        contentValues.put(DatabaseOpenHelper.VISIT_HISTORY_PLACE_ID,visitHistory.PlaceId)
        open()
        sqLiteDatabase!!.insert(DatabaseOpenHelper.TABLE_VISIT_HISTORY, null, contentValues)
        close()
        changeVisitState(visitHistory.PlaceId!!)
    }

    private fun selectVisitHistory(placeId: Int):Cursor{
        val selectAllQuery = "Select * from ${DatabaseOpenHelper.TABLE_VISIT_HISTORY} WHERE ${DatabaseOpenHelper.VISIT_HISTORY_PLACE_ID} = $placeId"
        return sqLiteDatabase!!.rawQuery(selectAllQuery, null)
    }

    @SuppressLint("Range")
    fun getVisitHistory(placeId: Int):ArrayList<VisitHistory>{
        val visitList = ArrayList<VisitHistory>()
        open()
        val cursor = selectVisitHistory(placeId)
        if (cursor.moveToFirst()) {
            do {
                val visitHistory = VisitHistory()
                visitHistory.Id = cursor.getInt(0)
                visitHistory.Date = DateHelper.fromDbDateFormat(cursor.getString(cursor.getColumnIndex(DatabaseOpenHelper.VISIT_HISTORY_DATE)))
                visitHistory.Description = cursor.getString(cursor.getColumnIndex(DatabaseOpenHelper.VISIT_HISTORY_DESCRIPTION))
                visitList.add(visitHistory)
            } while (cursor.moveToNext())
        }
        close()

        return visitList
    }
}