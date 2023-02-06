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

        String requestBody = request.getReader().lines().collect(Collectors.joining());
        String[] lines = requestBody.split("\n");

        File file = new File("helloWorld.txt");
        try {


            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write("Hello, World!");
            for (String line : lines) {
                fileWriter.write(line + System.lineSeparator());
            }
            fileWriter.close();


            String path = System.getenv("GOOGLE_APPLICATION_CREDENTIALS");

            GoogleCredentials googleCredentials;
            if (path == null) {
                throw new RuntimeException("Environment variable GOOGLE_APPLICATION_CREDENTIALS is not set");
            }
            try {
                 googleCredentials = GoogleCredentials.fromStream(new FileInputStream(path));
            } catch (FileNotFoundException e) {
                throw new RuntimeException("File not found at the specified path: " + path, e);
            }

            StorageOptions storageOptions = StorageOptions.newBuilder().setProjectId("maximal-radius-375114").setCredentials(googleCredentials).build();
            Storage storage = storageOptions.getService();



            BlobId blobId = BlobId.of("prost--invoice-pdf-bucket", "helloWorld.txt");
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("application/txt").build();
            byte[] content = Files.readAllBytes(Paths.get("helloWorld.txt"));
            Blob blob = storage.create(blobInfo);
            System.out.println(blob.getName());
            System.out.println(blob);



        } catch (Exception e) {
            e.printStackTrace();
        }


        Map<String, String> data = new ObjectMapper().readValue(requestBody,new TypeReference<Map<String, String>>() {});


        writer.write("Hello, World from Invoicing function! User: " + data.toString());
    }



}
