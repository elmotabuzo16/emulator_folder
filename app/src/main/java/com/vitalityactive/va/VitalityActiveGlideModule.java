package com.vitalityactive.va;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;

import java.io.InputStream;

import javax.inject.Inject;

import okhttp3.OkHttpClient;

@GlideModule(glideName = "VAGlide")
public class VitalityActiveGlideModule extends AppGlideModule {
    @Inject
    OkHttpClient httpClient;

    @Override
    public void registerComponents(Context context, Glide glide, Registry registry) {
        inject(context);
        useOkHttpClient(glide);
    }

    private void inject(Context context) {
        VitalityActiveApplication application = (VitalityActiveApplication) context.getApplicationContext();
        application.getDependencyInjector().inject(this);
    }

    private void useOkHttpClient(Glide glide) {
        OkHttpUrlLoader.Factory factory = new OkHttpUrlLoader.Factory(httpClient);
        glide.getRegistry().replace(GlideUrl.class, InputStream.class, factory);
    }
}
