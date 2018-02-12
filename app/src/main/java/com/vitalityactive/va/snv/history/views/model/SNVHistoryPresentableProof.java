package com.vitalityactive.va.snv.history.views.model;

import java.util.List;

/**
 * Created by dharel.h.rosell on 12/13/2017.
 */

public class SNVHistoryPresentableProof {

    public String title;
    public List<String> imagePaths;

    public SNVHistoryPresentableProof(String title, List<String> imagePaths) {
        this.title = title;
        this.imagePaths = imagePaths;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getImagePaths() {
        return imagePaths;
    }

    public void setImagePaths(List<String> imagePaths) {
        this.imagePaths = imagePaths;
    }
}
