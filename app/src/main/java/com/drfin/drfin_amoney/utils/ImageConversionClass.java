package com.drfin.drfin_amoney.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by Pulkit Kumar
 */
public class ImageConversionClass {

    public static String ImageToString(Context context, Uri ImageUri) {
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), ImageUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert bitmap != null;
        return loadImage(getBytesFromBitmap(bitmap));
    }

    public static String BitmapToString(Bitmap bitmapdata) {
        Bitmap bitmap;
        bitmap = bitmapdata;
        assert bitmap != null;
        return loadImage(getBytesFromBitmap(bitmap));
    }

    private static byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, stream);
        return stream.toByteArray();
    }

    private static String loadImage(byte[] imageData) {
        final String image64 = android.util.Base64.encodeToString(
                imageData, android.util.Base64.DEFAULT);
        Log.d("TAG", "loadImage: " + image64);
        return image64;
    }


    public static String BitmapTolowString(Bitmap bitmapdata) {
        Bitmap bitmap;
        bitmap = bitmapdata;
        assert bitmap != null;
        return loadImagelow(getByteslowFromBitmap(bitmap));
    }

    private static String loadImagelow(byte[] imageData) {
        final String image64 = android.util.Base64.encodeToString(
                imageData, Base64.NO_WRAP);
        Log.d("TAG", "loadImage: " + image64);
        return image64;
    }

    private static byte[] getByteslowFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 10, stream);
        return stream.toByteArray();
    }
}
