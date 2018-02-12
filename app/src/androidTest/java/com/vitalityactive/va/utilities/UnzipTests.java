package com.vitalityactive.va.utilities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.vitalityactive.va.MockJUnitAndCucumberRunner;
import com.vitalityactive.va.TestUtilities;
import com.vitalityactive.va.testutilities.annotations.FileSystemTests;

import org.junit.After;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.InputStream;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

@FileSystemTests
public class UnzipTests {
    @Before
    public void setUp() {
        Assume.assumeTrue(hasStoragePermissions());
        removeExtractedFiles();
    }

    private boolean hasStoragePermissions() {
        return isLollipopOrBelow() || (isPermissionGranted(Manifest.permission.READ_EXTERNAL_STORAGE) && isPermissionGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE));
    }

    private boolean isLollipopOrBelow() {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean isPermissionGranted(String permission) {
        return MockJUnitAndCucumberRunner.getInstance().getApplication().checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }

    @After
    public void tearDown() {
        removeExtractedFiles();
    }

    private void removeExtractedFiles() {
        File dir = new File(MockJUnitAndCucumberRunner.getRootDirectoryBasedOnAppName(), UnzipUtility.ZIP_CACHE);
        FileUtilities.removeRecursively(dir);
    }

    @Test
    public void can_extract_files() {
        InputStream contents = new TestUtilities().getResourceAsStream("zip/contents.zip");
        UnzipUtility.UnzipDetails unzip = UnzipUtility.unzip(MockJUnitAndCucumberRunner.getRootDirectoryBasedOnAppName(), contents);
        assertTrue("unzip failed: " + unzip.getErrorMessage(), unzip.success);

        File expectedFile1 = new File(MockJUnitAndCucumberRunner.getRootDirectoryBasedOnAppName(), UnzipUtility.ZIP_CACHE + File.separator + "contents/partnerx.json");
        File expectedFile2 = new File(MockJUnitAndCucumberRunner.getRootDirectoryBasedOnAppName(), UnzipUtility.ZIP_CACHE + File.separator + "contents/partnerx.png");

        assertTrue(expectedFile1.getPath() + " not found", expectedFile1.exists());
        assertTrue(expectedFile2.getPath() + " not found", expectedFile2.exists());
    }

    @Test
    public void can_remove_extracted_files() {
        InputStream contents = new TestUtilities().getResourceAsStream("zip/contents.zip");
        UnzipUtility.unzip(MockJUnitAndCucumberRunner.getRootDirectoryBasedOnAppName(), contents);

        UnzipUtility.removeAllFiles(MockJUnitAndCucumberRunner.getRootDirectoryBasedOnAppName());

        File expectedFile1 = new File(MockJUnitAndCucumberRunner.getRootDirectoryBasedOnAppName(), UnzipUtility.ZIP_CACHE + File.separator + "contents/partnerx.json");
        assertFalse(expectedFile1.getPath() + " still exists", expectedFile1.exists());
    }

    @Test
    public void return_unzipped_files() {
        InputStream contents = new TestUtilities().getResourceAsStream("zip/contents.zip");
        UnzipUtility.UnzipDetails unzip = UnzipUtility.unzip(MockJUnitAndCucumberRunner.getRootDirectoryBasedOnAppName(), contents);
        assertTrue("unzip failed: " + unzip.getErrorMessage(), unzip.success);

        File expectedFile1 = new File(MockJUnitAndCucumberRunner.getRootDirectoryBasedOnAppName(), UnzipUtility.ZIP_CACHE + File.separator + "contents/partnerx.json");
        File expectedFile2 = new File(MockJUnitAndCucumberRunner.getRootDirectoryBasedOnAppName(), UnzipUtility.ZIP_CACHE + File.separator + "contents/partnerx.png");

        assertTrue(unzip.files.contains(expectedFile1.getAbsolutePath()));
        assertTrue(unzip.files.contains(expectedFile2.getAbsolutePath()));
    }
}
