package dsow.besthackentrancetask.Database;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

import dsow.besthackentrancetask.R;

public class DbBitmapUtility {
    // convert from bitmap to byte array
    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }
    // convert from byte array to bitmap
    public static Bitmap getImage(byte[] image) {
        if (image==null)return BitmapFactory.decodeResource(Resources.getSystem(), R.id.header);
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
}
