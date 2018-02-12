package com.vitalityactive.va.utilities.photohandler;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.vitalityactive.va.R;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.events.EventListener;
import com.vitalityactive.va.uicomponents.AlertDialogFragment;
import com.vitalityactive.va.utilities.TextUtilities;

public class CameraGalleryInvokerImpl implements CameraGalleryInvoker, EventListener<AlertDialogFragment.DismissedEvent> {

    private Fragment fragmentCallBack;
    private AppCompatActivity activityCallBack;
    private ImageSource imgSource;
    private Uri captureImageOutputUri;
    private Context context;
    private EventDispatcher eventDispatcher;

    private String title = "Camera";
    private String description = "Photo";
    private static final String ERROR_DIALOG_TAG = "CAMERA_ERROR";

    public CameraGalleryInvokerImpl(EventDispatcher eventDispatcher) {
        this.eventDispatcher = eventDispatcher;
        this.eventDispatcher.addEventListener(AlertDialogFragment.DismissedEvent.class, this);
    }

    @Override
    public void start() {
        try {
            switch (imgSource) {
                case CAMERA:
                    if (CameraGalleryUtility.isLollipopOrCameraReadWriteAllowed(context)) {
                        captureImageOutputUri = CameraGalleryImageFetcher.getCaptureImageOutputUri(context, title, description);
                        Intent takePictureIntent = CameraGalleryUtility.getTakePictureIntent(captureImageOutputUri, context);

                        if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {
                            if (activityCallBack != null) {
                                activityCallBack.startActivityForResult(takePictureIntent, CameraGalleryUtility.IMAGE_SOURCE_CAMERA);
                            } else {
                                fragmentCallBack.startActivityForResult(takePictureIntent, CameraGalleryUtility.IMAGE_SOURCE_CAMERA);
                            }
                        }
                    } else {
                        if (activityCallBack != null) {
                            activityCallBack.requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    CameraGalleryUtility.REQUEST_IMAGE_CAPTURE);
                        } else {
                            fragmentCallBack.requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    CameraGalleryUtility.REQUEST_IMAGE_CAPTURE);
                        }
                    }
                    break;
                case GALLERY:
                    if (CameraGalleryUtility.isLollipopOrReadAllowed(context)) {
                        Intent pickImageIntent = CameraGalleryUtility.getPickImageIntent();

                        if (activityCallBack != null) {
                            activityCallBack.startActivityForResult(pickImageIntent, CameraGalleryUtility.IMAGE_SOURCE_GALLERY);
                        } else {
                            fragmentCallBack.startActivityForResult(pickImageIntent, CameraGalleryUtility.IMAGE_SOURCE_GALLERY);
                        }
                    } else {
                        if (activityCallBack != null) {
                            activityCallBack.requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    CameraGalleryUtility.REQUEST_READ_EXTERNAL_STORAGE);
                        } else {
                            fragmentCallBack.requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    CameraGalleryUtility.REQUEST_READ_EXTERNAL_STORAGE);
                        }

                    }
                    break;
            }
        } catch (Exception e) {
            showStartCameraError();
        }
    }

    @Override
    public boolean cropImage(Uri uri) {
        try {
            Intent cropIntent = CameraGalleryUtility.getCropImageIntent(uri, context);
            if (activityCallBack != null) {
                activityCallBack.startActivityForResult(cropIntent, CameraGalleryUtility.IMAGE_CROP);
            } else {
                fragmentCallBack.startActivityForResult(cropIntent, CameraGalleryUtility.IMAGE_CROP);
            }
        } catch (ActivityNotFoundException e) {
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
            return false;
        } catch (Exception e) {
            String errorMessage = "Whoops - Something went wrong...";
            Toast toast = Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
        return true;
    }

    @Override
    public void showStartCameraError() {
        FragmentManager fragmentManager;
        String title;
        String message;
        String ok;
        String settings;
        if (activityCallBack != null) {
            title = activityCallBack.getResources().getString(R.string.proof_cannot_access_camera_alert_title_175);
            message = activityCallBack.getResources().getString(R.string.proof_cannot_access_camera_alert_message_176);
            ok = activityCallBack.getResources().getString(R.string.ok_button_title_40);
            fragmentManager = activityCallBack.getSupportFragmentManager();
            settings = activityCallBack.getResources().getString(R.string.proof_settings_alert_button_174);

        } else {
            title = fragmentCallBack.getResources().getString(R.string.proof_cannot_access_camera_alert_title_175);
            message = fragmentCallBack.getResources().getString(R.string.proof_cannot_access_camera_alert_message_176);
            ok = fragmentCallBack.getResources().getString(R.string.ok_button_title_40);
            fragmentManager = fragmentCallBack.getFragmentManager();
            settings = fragmentCallBack.getResources().getString(R.string.proof_settings_alert_button_174);
        }

        AlertDialogFragment alert = AlertDialogFragment.create(
                ERROR_DIALOG_TAG,
                title,
                message,
                ok,
                null,
                settings);
        alert.show(fragmentManager, ERROR_DIALOG_TAG);
    }

    @Override
    public boolean saveToInternalStorageByUri(Uri uri) {
        Bitmap profileBitmap = CameraGalleryUtility.decodeSampledBitmapFromResource(500, 500, context, uri);
        String fileDirectoryOutput = CameraGalleryUtility.saveToInternalStorage(profileBitmap, context);
        return !TextUtilities.isNullOrEmpty(fileDirectoryOutput);
    }

    @Override
    public boolean saveToInternalStorageByBitmap(Bitmap bitmap) {
        String fileDirectoryOutput = CameraGalleryUtility.saveToInternalStorage(bitmap, context);
        return !TextUtilities.isNullOrEmpty(fileDirectoryOutput);
    }

    @Override
    public Uri getCroppedImageUri(Bitmap src) {
        return CameraGalleryUtility.getCroppedImageUri(context, src, "profile");
    }

    @Override
    public void persistWritePermission(Uri uri) {
        CameraGalleryUtility.persistWritePermission(context, uri);
    }

    @Override
    public void revokeWritePermissions(Uri uri) {
        CameraGalleryUtility.revokeWritePermission(context, uri);
    }

    @Override
    public void setFragmentCallback(Fragment fragmentCallback) {
        this.fragmentCallBack = fragmentCallback;
        context = this.fragmentCallBack.getActivity();
    }

    @Override
    public void setActivityCallBack(AppCompatActivity activity) {
        this.activityCallBack = activity;
        context = activity.getApplicationContext();
    }

    @Override
    public void setCapturedImageDetails(String title, String description) {
        this.title = title;
        this.description = description;
    }

    @Override
    public Uri getCapturedImageUri() {
        return captureImageOutputUri;
    }

    public byte[] getImageAsByteArray(Uri uri) {
        return CameraGalleryImageFetcher.getImageFileAsByteArray(uri.toString(), context);
    }

    @Override
    public void setInvokerMode(ImageSource imgSource) {
        this.imgSource = imgSource;
    }

    @Override
    public void finish() {
        eventDispatcher.removeEventListener(AlertDialogFragment.DismissedEvent.class, this);
        fragmentCallBack = null;
        imgSource = null;
        captureImageOutputUri = null;
        context = null;
    }

    @Override
    public void onEvent(AlertDialogFragment.DismissedEvent event) {
        if (event.getType().equals(ERROR_DIALOG_TAG) && event.getClickedButtonType() == AlertDialogFragment.DismissedEvent.ClickedButtonType.Positive) {
            try {
                startAppSettingsActivity();
            } catch (ActivityNotFoundException e) {
                // TODO show toast or snackbar message here
            }
        }
    }

    private void startAppSettingsActivity() {
        String packageName;
        Intent intent = new Intent();

        if (activityCallBack != null) {
            packageName = activityCallBack.getPackageName();
        } else {
            packageName = context.getPackageName();
        }

        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", packageName, null);
        intent.setData(uri);

        if (activityCallBack != null) {
            activityCallBack.startActivity(intent);
        } else {
            fragmentCallBack.startActivity(intent);
        }
    }
}
