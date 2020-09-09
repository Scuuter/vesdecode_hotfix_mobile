package com.travels.searchtravels;

import com.google.api.services.vision.v1.model.AnnotateImageResponse;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.EntityAnnotation;
import com.travels.searchtravels.utils.GoogleResponseCreator;

import java.util.ArrayList;
import java.util.List;

public class GoogleResponseCreatorMocks {

    public static GoogleResponseCreator getMockForCategory(String category) {
        return (bitmap, token) -> createResponse(category);
    }

    private static BatchAnnotateImagesResponse createResponse(String category) {
        List<AnnotateImageResponse> responses = new ArrayList<>();
        List<EntityAnnotation> entityAnnotations = new ArrayList<>();
        entityAnnotations.add(new EntityAnnotation().setDescription("\"" + category + "\""));
        responses.add(new AnnotateImageResponse().setTextAnnotations(entityAnnotations));
        return new BatchAnnotateImagesResponse().setResponses(responses);
    }

}
