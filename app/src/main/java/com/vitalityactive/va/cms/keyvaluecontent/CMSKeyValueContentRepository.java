package com.vitalityactive.va.cms.keyvaluecontent;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.vitalityactive.va.appconfig.AppConfigRepository;
import com.vitalityactive.va.nonsmokersdeclaration.NonSmokersDeclarationContent;
import com.vitalityactive.va.snv.learnmore.content.ScreeningDeclarationContent;
import com.vitalityactive.va.utilities.FileUtilities;
import com.vitalityactive.va.vhc.content.VHCContent;
import com.vitalityactive.va.vhr.content.VHRContent;
import com.vitalityactive.va.vhr.content.VHRKeyValueContent;
import com.vitalityactive.va.vna.content.VNAContent;
import com.vitalityactive.va.vna.content.VNAKeyValueContent;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class CMSKeyValueContentRepository {
    private Gson gson;
    private final File rootFilesDirectory;
    private final AppConfigRepository appConfigRepository;
    private final FallbackContentInputStreamLoader fallbackContentInputStreamLoader;
    private HashMap<String, CMSKeyValueContent> content = new HashMap<>();

    public CMSKeyValueContentRepository(Gson gson, File rootFilesDirectory, AppConfigRepository appConfigRepository, FallbackContentInputStreamLoader fallbackContentInputStreamLoader) {
        this.gson = gson;
        this.rootFilesDirectory = rootFilesDirectory;
        this.appConfigRepository = appConfigRepository;
        this.fallbackContentInputStreamLoader = fallbackContentInputStreamLoader;
    }

    public CMSKeyValueContent getContent(String languageCode) {
        if (!content.containsKey(languageCode)) {
            buildContent(languageCode);
        }
        return content.get(languageCode);
    }

    private void buildContent(String languageCode) {
        Loader loader = new Loader(languageCode);
        CMSKeyValueContent content = new CMSKeyValueContent();
        content.nonSmokersDeclarationContent = loader.loadNonSmokersContent();
        content.vhcContent = loader.loadVHCContent();
        content.vhrContent = loader.loadVHRContent();
        content.vnaContent = loader.loadVNAContent();
        content.scrContent =  loader.loadSCRContent();

        this.content.put(languageCode, content);
    }

    private class Loader {
        private final String languageCode;

        Loader(String languageCode) {
            this.languageCode = languageCode;
        }

        private NonSmokersDeclarationKeyValueContent loadNonSmokersContent() {
            return loadContent(
                    NonSmokersDeclarationKeyValueContent.class,
                    new NonSmokersDeclarationKeyValueContent(), AppConfigRepository.ResourceFileFeatureType.NON_SMOKERS_DECLARATION);
        }

        private VHCContent loadVHCContent() {
            return loadContent(
                    VHCKeyValueContent.class,
                    new VHCKeyValueContent(),
                    AppConfigRepository.ResourceFileFeatureType.VHC
            );
        }

        private VHRKeyValueContent loadVHRContent() {
            return loadContent(
                    VHRKeyValueContent.class,
                    new VHRKeyValueContent(), AppConfigRepository.ResourceFileFeatureType.VHR);
        }

        private VNAKeyValueContent loadVNAContent() {
            return loadContent(
                    VNAKeyValueContent.class,
                    new VNAKeyValueContent(), AppConfigRepository.ResourceFileFeatureType.VNA);
        }

        private ScreeningKeyValueContent loadSCRContent() {
            return loadContent(
                    ScreeningKeyValueContent.class,
                    new ScreeningKeyValueContent(), AppConfigRepository.ResourceFileFeatureType.SRC);
        }

        private <T> T loadContent(Class<T> clazz, T defaultValue, AppConfigRepository.ResourceFileFeatureType resourceFileFeatureType) {
            File file = buildFile(resourceFileFeatureType);
            File defaultLanguageFile = new File(rootFilesDirectory, appConfigRepository.getResourceFileName(resourceFileFeatureType, appConfigRepository.getDefaultLanguageCode()));
            InputStream fallbackContentInputStream = fallbackContentInputStreamLoader.open(resourceFileFeatureType.getFallbackFileName());
            return loadContent(file, defaultLanguageFile, fallbackContentInputStream, clazz, defaultValue);
        }

        @NonNull
        private File buildFile(AppConfigRepository.ResourceFileFeatureType resourceFileFeatureType) {
            return new File(rootFilesDirectory, appConfigRepository.getResourceFileName(resourceFileFeatureType, languageCode));
        }

        private <T> T loadContent(File file, File defaultLanguageFile, InputStream fallbackContentInputStream, Class<T> clazz, T defaultValue) {
            T result = loadContentFromFile(file, clazz);
            if (result == null) {
                result = loadContentFromFile(defaultLanguageFile, clazz);
            }
            if (result == null) {
                result = readFromFallbackContentInputStream(fallbackContentInputStream, clazz);
            }
            if (result == null) {
                result = defaultValue;
            }
            return result;
        }

        private <T> T loadContentFromFile(File file, Class<T> clazz) {
            try (FileInputStream fin = new FileInputStream(file)) {
                return gson.fromJson(FileUtilities.readFile(fin), clazz);
            } catch (FileNotFoundException ignored) {
                return null;
            } catch (IOException ignored) {
                return null;
            }
        }

        private <T> T readFromFallbackContentInputStream(InputStream defaultContentInputStream, Class<T> clazz) {
            try {
                return gson.fromJson(FileUtilities.readFile(defaultContentInputStream), clazz);
            } catch (Exception ignored) {
                return null;
            }
        }
    }

    public static class CMSKeyValueContent {
        private NonSmokersDeclarationContent nonSmokersDeclarationContent;
        private VHCContent vhcContent;
        private VHRKeyValueContent vhrContent;
        private VNAContent vnaContent;
        private ScreeningDeclarationContent scrContent;

        public NonSmokersDeclarationContent getNonSmokersDeclarationContent() {
            return nonSmokersDeclarationContent;
        }

        public VHCContent getVhcContent() {
            return vhcContent;
        }

        public VHRContent getVHRContent() {
            return vhrContent;
        }

        public VNAContent getVNAContent() {
            return vnaContent;
        }

        public ScreeningDeclarationContent getSCRContent() {
            return scrContent;
        }
    }
}
