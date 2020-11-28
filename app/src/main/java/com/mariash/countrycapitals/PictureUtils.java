package com.mariash.countrycapitals;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;

//Image scaling class
public class PictureUtils {

    public static Bitmap getScaledBitmap(Resources res, int id, Activity activity) {
        Point size = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(size);
        return getScaledBitmap(res, id, size.x, size.y);
    }

    public static Bitmap getScaledBitmap(Resources res, int id, int destWidth, int destHeight) {
        //Read image sizes on drawable
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, id,  options);

        float srcWidth = options.outWidth;
        float srcHeight = options.outHeight;

        //Calculate the degree of scaling
        int inSampleSize = 1;
        if (srcHeight > destHeight || srcWidth > destWidth) {
            float heightScale = srcHeight / destHeight;
            float widthScale = srcWidth / destWidth;
            inSampleSize = Math.round(heightScale > widthScale ? heightScale : widthScale);
        }
        options = new BitmapFactory.Options();
        options.inSampleSize = inSampleSize;

        //Read data and create a final image
        return BitmapFactory.decodeResource(res, id,  options);
    }

}
