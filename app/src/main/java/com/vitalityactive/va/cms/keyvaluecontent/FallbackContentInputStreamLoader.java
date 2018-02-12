package com.vitalityactive.va.cms.keyvaluecontent;

import java.io.InputStream;

public interface FallbackContentInputStreamLoader {
    InputStream open(String fileName);
}
