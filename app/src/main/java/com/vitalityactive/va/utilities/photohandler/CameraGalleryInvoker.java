package com.vitalityactive.va.utilities.photohandler;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

public interface CameraGalleryInvoker {

    enum ImageSource {
        CAMERA,
        GALLERY
    }

    void setFragmentCallback(Fragment fragmentCallback);

    void setActivityCallBack(AppCompatActivity activity);

    void setCapturedImageDetails(String title, String description);

    void setInvokerMode(ImageSource source);

    void showStartCameraError();

    void start();

    void persistWritePermission(Uri uri);

    void revokeWritePermissions(Uri uri);

    void finish();

    boolean saveToInternalStorageByUri(Uri uri);

    boolean saveToInternalStorageByBitmap(Bitmap bitmap);

    boolean cropImage(Uri uri);

    Uri getCapturedImageUri();

    byte[] getImageAsByteArray(Uri uri);

    Uri getCroppedImageUri(Bitmap src);

}


