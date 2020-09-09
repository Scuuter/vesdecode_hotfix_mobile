package com.travels.searchtravels.api;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.VisibleForTesting;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.travels.searchtravels.utils.GoogleResponseCreator;
import com.travels.searchtravels.utils.GoogleResponseCreatorImpl;

import java.io.IOException;

public class VisionApi{

    private static GoogleResponseCreator googleResponseCreator = new GoogleResponseCreatorImpl();

    public static void findLocation(Bitmap bitmap, String token, OnVisionApiListener onVisionApiListener) {
        Handler handler = new Handler(Looper.getMainLooper());
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    BatchAnnotateImagesResponse response = googleResponseCreator.getResponse(bitmap, token);
                    try {
                        if (response != null && response.getResponses() != null && response.getResponses().get(0) != null && response.getResponses().get(0).getLandmarkAnnotations() != null && response.getResponses().get(0).getLandmarkAnnotations().get(0) != null && response.getResponses().get(0).getLandmarkAnnotations().get(0).getLocations() != null && response.getResponses().get(0).getLandmarkAnnotations().get(0).getLocations().get(0) != null && response.getResponses().get(0).getLandmarkAnnotations().get(0).getLocations().get(0).getLatLng() != null) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    onVisionApiListener.onSuccess(response.getResponses().get(0).getLandmarkAnnotations().get(0).getLocations().get(0).getLatLng());

                                }
                            });
                        } else if (response != null) {
                            if (response.toString().toLowerCase().contains("\"sea\"")) {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        onVisionApiListener.onErrorPlace("sea");
                                    }
                                });

                            } else if (response.toString().toLowerCase().contains("\"beach\"")) {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        onVisionApiListener.onErrorPlace("beach");
                                    }
                                });

                            } else if (response.toString().toLowerCase().contains("\"mountain\"")) {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        onVisionApiListener.onErrorPlace("mountain");
                                    }
                                });

                            } else if (response.toString().toLowerCase().contains("\"snow\"")) {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        onVisionApiListener.onErrorPlace("snow");
                                    }
                                });

                            } else if (response.toString().toLowerCase().contains("\"ocean\"")) {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        onVisionApiListener.onErrorPlace("ocean");
                                    }
                                });

                            } else {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        onVisionApiListener.onError();
                                    }
                                });

                            }

                        }

                        System.out.println("Cloud Vision success = " + response);


                    } catch (Error e) {
                        e.printStackTrace();
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                onVisionApiListener.onError();
                            }
                        });
                    }
                } catch (GoogleJsonResponseException e) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            onVisionApiListener.onError();
                        }
                    });
                    Log.e("VISION_API", "Request failed: " + e.getContent());
                } catch (IOException e) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            onVisionApiListener.onError();
                        }
                    });
                    Log.d("VISION_API", "Request failed: " + e.getMessage());
                }


            }
        });
        thread.start();
    }

    @VisibleForTesting
    public static void setGoogleResponseCreator(GoogleResponseCreator creator) {
        googleResponseCreator = creator;
    }

}
