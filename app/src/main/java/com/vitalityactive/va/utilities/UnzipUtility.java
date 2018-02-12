package com.vitalityactive.va.utilities;

import android.content.Context;
import android.os.Environment;

import com.vitalityactive.va.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class UnzipUtility {
    static final String ZIP_CACHE = "zip-cache";

    @SuppressWarnings("unused")         // useful(?) overload for use in the app eventually
    public static UnzipDetails unzip(Context context, InputStream inputStream) {
        File appRootDirectory = new File(Environment.getExternalStorageDirectory(), context.getString(R.string.app_name));
        return unzip(appRootDirectory, inputStream);
    }

    public static UnzipDetails unzip(File applicationRootDirectory, InputStream inputStream) {
        try {
            File cacheDir = new File(applicationRootDirectory, ZIP_CACHE);
            List<String> extractedFiles = tryUnzip(inputStream, cacheDir);
            return new UnzipDetails(extractedFiles);
        } catch (IOException error) {
            return new UnzipDetails(error);
        }
    }

    public static void removeAllFiles(File applicationRootDirectory) {
        FileUtilities.removeRecursively(applicationRootDirectory);
    }

    private static List<String> tryUnzip(InputStream inputStream, File extractTo) throws IOException {
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        List<String> extractedFiles = extractAllEntries(zipInputStream, extractTo);
        zipInputStream.close();
        return extractedFiles;
    }

    private static List<String> extractAllEntries(ZipInputStream zipInputStream, File extractTo) throws IOException {
        ZipEntry entry;
        ArrayList<String> files = new ArrayList<>();
        while ((entry = zipInputStream.getNextEntry()) != null) {
            String s = extractEntry(zipInputStream, entry, extractTo);
            if (s != null) {
                files.add(s);
            }
            zipInputStream.closeEntry();
        }
        return files;
    }

    private static String extractEntry(ZipInputStream zipInputStream, ZipEntry entry, File extractTo) throws IOException {
        File outputFile = new File(extractTo.getPath(), entry.getName());
        if (entry.isDirectory()) {
            //noinspection ResultOfMethodCallIgnored
            outputFile.mkdirs();
            return null;
        }

        FileOutputStream outputStream = new FileOutputStream(outputFile);
        extractFromZipStream(zipInputStream, outputStream);
        outputStream.close();

        return outputFile.getAbsolutePath();
    }

    private static void extractFromZipStream(ZipInputStream zipInputStream, FileOutputStream outputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = zipInputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, read);
        }
    }

    public static class UnzipDetails {
        private final Throwable error;
        public final boolean success;
        public final List<String> files;

        public UnzipDetails(List<String> extractedFiles) {
            this.error = null;
            this.success = true;
            files = extractedFiles;
        }

        public UnzipDetails(Throwable error) {
            this.error = error;
            this.success = false;
            files = new ArrayList<>();
        }

        public String getErrorMessage() {
            if (!success && error != null) {
                return error.getLocalizedMessage();
            }
            return "";
        }
    }
}
