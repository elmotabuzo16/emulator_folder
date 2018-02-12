package com.vitalityactive.va.vhc.addproof;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;

import com.vitalityactive.va.R;
import com.vitalityactive.va.utilities.TimeUtilities;
import com.vitalityactive.va.utilities.date.NonUserFacingDateFormatter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class ProofImageFetcher {

    private Context context;

    public ProofImageFetcher(Context context) {
        this.context = context;
    }

    static Uri getCaptureImageOutputUri(Context context) {
        return FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", getImageFileWithGeneratedName(context));
    }

    @NonNull
    private static File getImageFileWithGeneratedName(Context context) {
        return getImageFile(context, "proof_item"+ NonUserFacingDateFormatter.getAllFieldsWithoutSeparatorsFormatter().format(new TimeUtilities().now()) +".jpg");
    }

    private static File getImageFile(Context context, String fileName) {
        return new File(getImageFileDirectory(context).getPath(), fileName);
    }

    @NonNull
    private static File getImageFileDirectory(Context context) {
        File imageFileDirectory = new File(android.os.Environment.getExternalStorageDirectory(), context.getString(R.string.app_name) + File.separator + "VHC" + File.separator + "Proof");
        //noinspection ResultOfMethodCallIgnored
        imageFileDirectory.mkdirs();
        return imageFileDirectory;
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int requiredWidth, int requiredHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 2;

        if (height > requiredHeight || width > requiredWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) requiredHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) requiredWidth);
            }
        }
        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(int reqWidth, int reqHeight, Context context, Uri imageUri) {
        try {
            InputStream input = null;
            try {
                input = context.getContentResolver().openInputStream(imageUri);
                if (input == null) {
                    return null;
                }
                final BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                Rect rect = new Rect();
                BitmapFactory.decodeStream(input, rect, options);
                options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

                if (input.markSupported()) {
                    input.reset();
                } else {
                    input.close();
                    input = context.getContentResolver().openInputStream(imageUri);
                }
                if (input == null) {
                    return null;
                }
                options.inJustDecodeBounds = false;
                return BitmapFactory.decodeStream(input, rect, options);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            } finally {
                if (input != null) {
                    input.close();
                }
            }
        } catch (Exception ignored) {
            return null;
        }
    }

    public byte[] getImageFileAsByteArray(String filePath) {
        Uri uri = Uri.parse(filePath);
        try {
            return getBytes(context.getContentResolver().openInputStream(uri));
        } catch (IOException e) {
            e.printStackTrace();
            return new byte[0];
        }
    }

    private byte[] getBytes(InputStream inputStream) throws IOException {
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
