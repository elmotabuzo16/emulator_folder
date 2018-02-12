package com.vitalityactive.va.utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.media.ExifInterface;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.request.RequestOptions;
import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.vitalityactive.va.VAGlide;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;

import okhttp3.Headers;
import okhttp3.Request;

public class ImageLoader {

    private static final int EXIF_ORIENTATION_TAG_ID = 274;

    private static int getExifOrientation(Context context, String uriString) throws IOException, ImageProcessingException, MetadataException {
        InputStream inputStream = context.getContentResolver().openInputStream(Uri.parse(uriString));
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            ExifInterface exifInterface = new ExifInterface(inputStream);
            return exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        } else {
            Metadata metadata;

            int exifOrientation = 0;
            metadata = ImageMetadataReader.readMetadata(inputStream);

            ExifIFD0Directory directories = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
            if (directories != null) {
                exifOrientation = directories.getInt(EXIF_ORIENTATION_TAG_ID);
            }

            return exifOrientation;
        }
    }

    private static int getRotationDegrees(int exifOrientation) {
        int degrees = 0;
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            degrees = 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            degrees = 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            degrees = 270;
        }
        return degrees;
    }

    private static int getExifOrientation(Context context, Uri uri) {
        try {
            return getExifOrientation(context, uri.toString());
        } catch (NullPointerException | IOException | MetadataException | ImageProcessingException e) {
            return 0;
        }
    }

    public static void clearCache(Context context) {
        VAGlide.get(context).clearDiskCache();
        VAGlide.get(context).clearMemory();
    }

    public static void loadImageFromUriAndRotateBasedOnExifDataAndCenterInside(Uri uri, ImageView imageView) {
        Context context = imageView.getContext();
        RequestOptions requestOptions = new RequestOptions().centerInside();
        int degrees = getRotationDegrees(getExifOrientation(context, uri));
        if (degrees != 0) {
            requestOptions = requestOptions.transform(new RotateTransformation(degrees));
        }
        VAGlide.with(context)
                .load(uri)
                .apply(requestOptions)
                .into(imageView);
    }

    @SuppressWarnings("unchecked")
    public static void loadImageFromUriRoundedAndRotateBasedOnExifDataAndCenterCrop(Uri uri, ImageView imageView) {
        Context context = imageView.getContext();
        RequestOptions requestOptions = new RequestOptions().centerCrop();
        int degrees = getRotationDegrees(getExifOrientation(context, uri));
        if (degrees != 0) {
            requestOptions = requestOptions.transforms(new RotateTransformation(degrees), new CircleTransform());
        } else {
            requestOptions = requestOptions.transform(new CircleTransform());
        }
        VAGlide.with(context)
                .load(uri)
                .apply(requestOptions)
                .into(imageView);
    }

    public static void loadImageFromUriRoundedAndCenterCrop(Uri uri, ImageView imageView) {
        Context context = imageView.getContext();
        RequestOptions requestOptions = new RequestOptions().centerCrop()
                .transform(new CircleTransform());
        VAGlide.with(context)
                .load(uri)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .apply(requestOptions)
                .into(imageView);
    }

    public static void loadImageFromUri(Uri uri, ImageView imageView) {
        VAGlide.with(imageView.getContext())
                .load(uri)
                .into(imageView);
    }

    @NonNull
    private static GlideUrl buildGlideUrlFromRetrofitRequest(Request request) {
        Headers headers = request.headers();

        LazyHeaders.Builder headerBuilder = new LazyHeaders.Builder();
        for (int i = 0; i < headers.size(); i++) {
            String header = headers.name(i);
            headerBuilder.addHeader(header, headers.get(header));
        }

        return new GlideUrl(request.url().url(), headerBuilder.build());
    }

    static RequestBuilder<Drawable> loadImageFromRetrofitRequest(Context context, Request request) {
        return VAGlide.with(context)
                .load(buildGlideUrlFromRetrofitRequest(request));
    }

    static void loadImage(RequestBuilder<Drawable> request, @DrawableRes int placeholder, @DrawableRes int error, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .error(error)
                .placeholder(placeholder);
        request.apply(options)
                .into(imageView);
    }

    private static class RotateTransformation extends BitmapTransformation {

        private float rotateRotationAngle = 0f;

        private RotateTransformation(float rotateRotationAngle) {
            this.rotateRotationAngle = rotateRotationAngle;
        }

        @Override
        protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
            Matrix matrix = new Matrix();

            matrix.postRotate(rotateRotationAngle);

            return Bitmap.createBitmap(toTransform, 0, 0, toTransform.getWidth(), toTransform.getHeight(), matrix, true);
        }

        @Override
        public void updateDiskCacheKey(MessageDigest messageDigest) {
            messageDigest.update(("rotate" + rotateRotationAngle).getBytes());
        }
    }

    private static class CircleTransform extends BitmapTransformation {

        @Override
        protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
            int size = Math.min(toTransform.getWidth(), toTransform.getHeight());
            int x = (toTransform.getWidth() - size) / 2;
            int y = (toTransform.getHeight() - size) / 2;

            Bitmap squared = Bitmap.createBitmap(toTransform, x, y, size, size);

            Bitmap result = pool.get(size, size, Bitmap.Config.ARGB_8888);

            Canvas canvas = new Canvas(result);
            Paint paint = new Paint();
            paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
            paint.setAntiAlias(true);
            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);
            return result;
        }

        @Override
        public void updateDiskCacheKey(MessageDigest messageDigest) {
            messageDigest.update(("circle").getBytes());
        }
    }
}
