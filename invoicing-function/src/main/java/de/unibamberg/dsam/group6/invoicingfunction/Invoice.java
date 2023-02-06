package de.unibamberg.dsam.group6.invoicingfunction;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.util.Value;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;
import com.google.cloud.storage.*;

import java.io.*;


import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.stream.Collectors;



public class Invoice implements HttpFunction {


    @Override
    public void service(HttpRequest request, HttpResponse response) throws Exception {
        BufferedWriter writer = response.getWriter();
        final var requestBody = request.getReader().lines().toList();
        try {
            StorageOptions storageOptions = StorageOptions.newBuilder().setProjectId("maximal-radius-375114")
                    .setCredentials(GoogleCredentials.getApplicationDefault())
                    .build();
            Storage storage = storageOptions.getService();

            BlobId blobId = BlobId.of("prost--invoice-pdf-bucket", "helloWorld.txt");
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("application/txt").build();
            Blob blob = storage.create(blobInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }

        writer.write("Hello, World from Invoicing function!");
    }
}
