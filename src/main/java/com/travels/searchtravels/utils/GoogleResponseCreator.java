package com.travels.searchtravels.utils;

import android.graphics.Bitmap;

import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;

import java.io.IOException;

public interface GoogleResponseCreator {
    BatchAnnotateImagesResponse getResponse(Bitmap bitmap, String token) throws IOException;
}
