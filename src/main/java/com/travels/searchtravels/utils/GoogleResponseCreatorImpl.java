package com.travels.searchtravels.utils;

import android.graphics.Bitmap;
import android.util.Log;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.travels.searchtravels.utils.ImageHelper.getBase64EncodedJpeg;

public class GoogleResponseCreatorImpl implements GoogleResponseCreator {

    public BatchAnnotateImagesResponse getResponse(Bitmap bitmap, String token) throws IOException {
        GoogleCredential credential = new GoogleCredential().setAccessToken(token);
        HttpTransport httpTransport = AndroidHttp.newCompatibleTransport();
        JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

        Vision.Builder builder = new Vision.Builder
                (httpTransport, jsonFactory, credential);
        Vision vision = builder.build();

        List<Feature> featureList = new ArrayList<>();

        Feature textDetection = new Feature();
        textDetection.setType("WEB_DETECTION");
        textDetection.setMaxResults(10);
        featureList.add(textDetection);

        Feature landmarkDetection = new Feature();
        landmarkDetection.setType("LANDMARK_DETECTION");
        landmarkDetection.setMaxResults(10);
        featureList.add(landmarkDetection);

        List<AnnotateImageRequest> imageList = new ArrayList<>();
        AnnotateImageRequest annotateImageRequest = new AnnotateImageRequest();
        Image base64EncodedImage = getBase64EncodedJpeg(bitmap);
        annotateImageRequest.setImage(base64EncodedImage);
        annotateImageRequest.setFeatures(featureList);
        imageList.add(annotateImageRequest);

        BatchAnnotateImagesRequest batchAnnotateImagesRequest =
                new BatchAnnotateImagesRequest();
        batchAnnotateImagesRequest.setRequests(imageList);

        Vision.Images.Annotate annotateRequest =
                vision.images().annotate(batchAnnotateImagesRequest);
        // Due to a bug: requests to Vision API containing large images fail when GZipped.
        annotateRequest.setDisableGZipContent(true);
        Log.d("GOOGLE_RESPONSE_CREATOR", "sending request");

        return annotateRequest.execute();
    }
}
