package com.example.coursefinderapp.util.image

import android.graphics.Bitmap
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.security.MessageDigest

class CropTopTransformation : BitmapTransformation() {

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update("CropTopTransformation".toByteArray(Charsets.UTF_8))
    }

    override fun transform(
        pool: BitmapPool,
        toTransform: Bitmap,
        outWidth: Int,
        outHeight: Int
    ): Bitmap {

        val scaleWidth = outWidth.toFloat() / toTransform.width
        val scaleHeight = outHeight.toFloat() / toTransform.height

        val scale = maxOf(scaleWidth, scaleHeight)

        val scaledWidth = (toTransform.width * scale).toInt()
        val scaledHeight = (toTransform.height * scale).toInt()

        val scaledBitmap = Bitmap.createScaledBitmap(
            toTransform, scaledWidth, scaledHeight, true
        )

        val top = 0
        return Bitmap.createBitmap(scaledBitmap, 0, top, outWidth, outHeight)
    }
}