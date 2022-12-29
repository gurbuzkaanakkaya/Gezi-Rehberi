package com.example.projectpazarama.Util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream
import java.sql.Blob

object ImageHelper {
    
    fun blobToBitmap(byteArray : ByteArray) : Bitmap{
        return BitmapFactory.decodeByteArray(byteArray,0,byteArray.size)
    }
    fun bitmapToBlob(bitmap : Bitmap) : ByteArray{
        val bitmapToBlob = ByteArrayOutputStream()
        val smallBitmap = makeSmallerBitmap(bitmap,300)
        smallBitmap.compress(Bitmap.CompressFormat.JPEG,100,bitmapToBlob)
        return bitmapToBlob.toByteArray()
    }

    private fun makeSmallerBitmap(image : Bitmap, maxSize : Int) : Bitmap{
        var with = image.width
        var height = image.height
        val bitmapRational : Double = with.toDouble() / height.toDouble()

        if (bitmapRational > 1){
            //landscape
            with = maxSize
            val scaleHeight = with / bitmapRational
            height = scaleHeight.toInt()
        }else{
            height = maxSize
            val scaledWith = height * bitmapRational
            with = scaledWith.toInt()
        }
        return Bitmap.createScaledBitmap(image,with,height,true)
    }
}