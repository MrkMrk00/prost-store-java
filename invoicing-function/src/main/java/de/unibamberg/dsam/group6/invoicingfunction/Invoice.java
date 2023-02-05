package de.unibamberg.dsam.group6.invoicingfunction;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;


import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

public class Invoice implements HttpFunction {

    @Override
    public void service(HttpRequest request, HttpResponse response) throws Exception {
        BufferedWriter writer = response.getWriter();

        String requestBody = request.getReader().lines().collect(Collectors.joining());
        String[] lines = requestBody.split("\n");


    Map<String, String> data = new ObjectMapper().readValue(requestBody,new TypeReference<Map<String, String>>() {});




        writer.write("Hello, World from Invoicing function! User: " + data.toString());
    }

}
