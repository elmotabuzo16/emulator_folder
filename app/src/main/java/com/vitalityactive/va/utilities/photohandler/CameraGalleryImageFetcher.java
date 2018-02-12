package com.vitalityactive.va.utilities.photohandler;

import android.content.Context;
import android.net.Uri;
import android.support.v4.content.FileProvider;

import com.vitalityactive.va.R;
import com.vitalityactive.va.utilities.TimeUtilities;
import com.vitalityactive.va.utilities.date.NonUserFacingDateFormatter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

class CameraGalleryImageFetcher {

    private static final String PACKAGE_SUFFIX = ".provider";

    static Uri getCaptureImageOutputUri(Context context, String fileTitle, String fileDesc) {
        String fullPackageName = context.getApplicationContext().getPackageName() + PACKAGE_SUFFIX;
        String fileName = fileDesc + "_" + NonUserFacingDateFormatter.getAllFieldsWithoutSeparatorsFormatter().format(new TimeUtilities().now()) +".jpg";
        String imageFileName = context.getString(R.string.app_name) + File.separator + fileTitle + File.separator + fileDesc;

        File imageFileDirectory = new File(android.os.Environment.getExternalStorageDirectory(), imageFileName );
        //noinspection ResultOfMethodCallIgnored
        imageFileDirectory.mkdirs();

        File imageFile = new File(imageFileDirectory.getPath(), fileName);

        return FileProvider.getUriForFile(context, fullPackageName , imageFile ) ;
    }

    static byte[] getImageFileAsByteArray(String filePath, Context context) {
        Uri uri = Uri.parse(filePath);
        try {
            return getBytes(context.getContentResolver().openInputStream(uri));
        } catch (IOException e) {
            e.printStackTrace();
            return new byte[0];
        }
    }

    private static byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }
}
