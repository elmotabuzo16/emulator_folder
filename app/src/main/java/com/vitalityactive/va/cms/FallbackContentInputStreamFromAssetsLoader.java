package com.vitalityactive.va.cms;

import android.content.Context;

import com.vitalityactive.va.cms.keyvaluecontent.FallbackContentInputStreamLoader;

import java.io.IOException;
import java.io.InputStream;

public class FallbackContentInputStreamFromAssetsLoader implements FallbackContentInputStreamLoader {
    private final Context context;

    public FallbackContentInputStreamFromAssetsLoader(Context context) {
        this.context = context;
    }

    @Override
    public InputStream open(String fileName) {
        try {
            return context.getAssets().open(fileName);
        } catch (IOException ignored) {
            return null;
        }
    }
}
