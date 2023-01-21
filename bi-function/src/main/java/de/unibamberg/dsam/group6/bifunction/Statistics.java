package de.unibamberg.dsam.group6.bifunction;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.BufferedWriter;

public class Statistics implements HttpFunction {
    private static final Gson gson = new Gson();
    @Override
    public void service(HttpRequest request, HttpResponse response) throws Exception {

        String contentType = request.getContentType().orElse("");
        if(!contentType.equals("application/json")){
            response.setStatusCode(400, "Bad content type!");
            return;
        }
        JsonObject body = gson.fromJson(request.getReader(), JsonObject.class);

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.getApplicationDefault())
                .build();

        FirebaseApp.initializeApp(options);
    }
}
