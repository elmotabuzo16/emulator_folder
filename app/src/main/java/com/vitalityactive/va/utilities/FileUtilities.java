package com.vitalityactive.va.utilities;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.vitalityactive.va.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import okhttp3.ResponseBody;

public class FileUtilities {
    // This method is duplicated in TestUtilities because the cucumber tests blow up if I move it to a library and depend on it in both the app and the tests
    public static String readFile(InputStream inputStream) {
        final StringBuilder sb = new StringBuilder();
        String strLine;
        try (final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"))) {
            while ((strLine = reader.readLine()) != null) {
                sb.append(strLine);
            }
        } catch (final IOException ignore) {
            //ignore
        }
        return sb.toString();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void removeRecursively(File file) {
        if (file.isDirectory() && file.listFiles() != null) {
            for (File child : file.listFiles()) {
                removeRecursively(child);
            }
        }
        file.delete();
    }

    @NonNull
    public static File getBaseApplicationDirectoryOnExternalStorage(Context context) {
        return new File(android.os.Environment.getExternalStorageDirectory(), context.getString(R.string.app_name));
    }

    @NonNull
    public static boolean writeFileToDisk(String filesDir, InputStream fileInput, String fileName) {

        Log.d("writeFileToDisk","writeFileToDisk");
        File file = new File(filesDir + File.separator + fileName);

        if(file.exists()){
            file.delete();
        }
        byte[] fileReader = new byte[4096];

        try (InputStream inputStream = fileInput; OutputStream outputStream = new FileOutputStream(file)) {
            while (true) {
                int read = inputStream.read(fileReader);
                if (read == -1) {
                    break;
                }
                outputStream.write(fileReader, 0, read);
            }
            outputStream.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String readJSONFromAsset(InputStream inputStream) {
        String json = null;
        try {
            InputStream is = inputStream;
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}
