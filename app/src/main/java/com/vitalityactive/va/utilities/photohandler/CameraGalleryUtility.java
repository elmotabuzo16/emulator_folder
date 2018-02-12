package com.vitalityactive.va.utilities.photohandler;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class CameraGalleryUtility {

    static final int REQUEST_READ_EXTERNAL_STORAGE = 1;
    static final int REQUEST_IMAGE_CAPTURE = 2;

    public static final int IMAGE_SOURCE_CAMERA = 0;
    public static final int IMAGE_SOURCE_GALLERY = 1;
    public static final int IMAGE_CROP = 2;

    private static final String CROP_ACTIVITY = "com.android.camera.action.CROP";

    @NonNull
    static Intent getTakePictureIntent(Uri captureImageOutputUri, Context context) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, captureImageOutputUri);
        grantWritePermission(context, takePictureIntent, captureImageOutputUri);
        return takePictureIntent;
    }

    @NonNull
    static Intent getPickImageIntent() {
        Intent pickImageIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        pickImageIntent.setType("image/*");
        pickImageIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        return pickImageIntent;
    }

    @NonNull
    static Intent getCropImageIntent(Uri imageUri, Context context) {
        Intent cropIntent = new Intent(CROP_ACTIVITY);
        cropIntent.setDataAndType(imageUri, "image/*");
        cropIntent.putExtra("crop", "true");
        cropIntent.putExtra("aspectX", 1);
        cropIntent.putExtra("aspectY", 1);
        cropIntent.putExtra("outputX", 120);
        cropIntent.putExtra("outputY", 120);
        cropIntent.putExtra("return-data", true);
        grantWritePermission(context, cropIntent, imageUri);
        return cropIntent;
    }

    static void persistWritePermission(@NonNull Context context, Uri uri) {
        if (!uri.getPath().equals("/external_files/tmp.png")) {
            context.getContentResolver().takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION & Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }
    }

    static void revokeWritePermission(@NonNull Context context, Uri uri) {
        context.revokeUriPermission(uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
    }

    static boolean isLollipopOrCameraReadWriteAllowed(Context context) {
        return CameraGalleryUtility.isLollipopOrBelow()
                || (CameraGalleryUtility.isPermissionGranted(Manifest.permission.CAMERA, context)
                && CameraGalleryUtility.isPermissionGranted(Manifest.permission.READ_EXTERNAL_STORAGE, context)
                && CameraGalleryUtility.isPermissionGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE, context));
    }

    static boolean isLollipopOrReadAllowed(Context context) {
        return CameraGalleryUtility.isLollipopOrBelow()
                || (CameraGalleryUtility.isPermissionGranted(Manifest.permission.READ_EXTERNAL_STORAGE, context)
                && CameraGalleryUtility.isPermissionGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE, context));
    }

    static Uri getCroppedImageUri(Context context, Bitmap src, String title) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        src.compress(Bitmap.CompressFormat.JPEG, 80, os);

        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), src, title, null);
        return Uri.parse(path);
    }

    static Bitmap decodeSampledBitmapFromResource(int reqWidth, int reqHeight, Context context, Uri imageUri) {
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

    static String saveToInternalStorage(Bitmap bitmapImage, Context context) {
        File directory = context.getFilesDir();
        File mypath = new File(directory, "profile.jpg");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
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

    private static void grantWritePermission(@NonNull Context context, Intent intent, Uri uri) {
        List<ResolveInfo> resInfoList = context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : resInfoList) {
            String packageName = resolveInfo.activityInfo.packageName;
            context.grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
    }

    private static boolean isLollipopOrBelow() {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private static boolean isPermissionGranted(String permission, Context context) {
        return context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }
}
