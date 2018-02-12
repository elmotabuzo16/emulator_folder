package com.vitalityactive.va.utilities;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;

import com.bumptech.glide.RequestBuilder;
import com.vitalityactive.va.appconfig.AppConfigRepository;
import com.vitalityactive.va.authentication.AccessTokenAuthorizationProvider;
import com.vitalityactive.va.cms.CMSService;

import okhttp3.Request;

public class CMSImageLoader extends ImageLoader {
    private final AccessTokenAuthorizationProvider accessTokenAuthorizationProvider;
    private final AppConfigRepository appConfigRepository;
    private final CMSService cmsService;

    public CMSImageLoader(CMSService cmsService, AppConfigRepository appConfigRepository,
                          AccessTokenAuthorizationProvider accessTokenAuthorizationProvider) {
        this.accessTokenAuthorizationProvider = accessTokenAuthorizationProvider;
        this.appConfigRepository = appConfigRepository;
        this.cmsService = cmsService;
    }

    private RequestBuilder<Drawable> buildRequest(Context context, String filename) {
        String authorization = accessTokenAuthorizationProvider.getAuthorization();
        String groupId = appConfigRepository.getLiferayGroupId();
        Request request = cmsService.getFileRequest(filename, groupId, authorization).request();
        return loadImageFromRetrofitRequest(context, request);
    }

    private void loadImage(ImageView imageView, String fileName, @DrawableRes int placeholderImageResourceId, @DrawableRes int errorImageResourceId) {
        RequestBuilder<Drawable> request = buildRequest(imageView.getContext(), fileName);
        loadImage(request, placeholderImageResourceId, errorImageResourceId, imageView);
    }

    public void loadImage(ImageView imageView, String fileName, @DrawableRes int placeholderErrorImageResourceId) {
        loadImage(imageView, fileName, placeholderErrorImageResourceId, placeholderErrorImageResourceId);
    }
}
