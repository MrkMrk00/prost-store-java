package de.unibamberg.dsam.group6.prost.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.http.HttpHeaders;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;


import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;


import java.io.File;
import java.io.FileInputStream;

import java.net.URI;
import java.util.*;


@Controller


public class InvoiceController {



    @GetMapping("/send_data")
    public ResponseEntity<String> sendData( @RequestParam String user,
                                           @RequestParam String amount,
                                           @RequestParam String date,
                                           @RequestParam String order_id,
                                           @RequestParam String address,
                                           @RequestParam String products
    ) throws Exception , JsonProcessingException {


        RestTemplate restTemplate = new RestTemplate();


        Map<String, String> dataJSON = new HashMap<>();
        dataJSON.put("user", user);
        dataJSON.put("order_id", order_id);
        dataJSON.put("amount", amount);
        dataJSON.put("date", date);
        dataJSON.put("address", address);
        dataJSON.put("products", products);


        String jsonData = new ObjectMapper().writeValueAsString(dataJSON);
        System.out.println(jsonData);



        HttpEntity<String> request = new HttpEntity<>(jsonData);

        ResponseEntity<String> response = restTemplate.postForEntity(
                "http://localhost:8081/", request, String.class);

        return response;
    }

    @GetMapping("/call-function")
    public String callFunction() throws Exception {


        File credentialsFile = new File("C:\\Users\\Besitzer\\IdeaProjects\\group6\\.secure-files\\maximal-radius-375114-c8c681e93685.json");



        List<String> scopes = Arrays.asList("https://www.googleapis.com/auth/cloud-platform");
        GoogleCredentials credentials = ServiceAccountCredentials.fromStream(new FileInputStream(credentialsFile))
                .createScoped(scopes);

        credentials.refreshIfExpired();


        String functionUrl = "https://europe-west3-maximal-radius-375114.cloudfunctions.net/invoicing-function";
        String accessToken = credentials.getAccessToken().getTokenValue();

        System.out.println(accessToken);


        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<HttpHeaders> entity = new HttpEntity<>(headers);

//            ResponseEntity<String> response = restTemplate.exchange(
//                functionUrl,
//                HttpMethod.POST,
//                entity,
//                String.class
//        );

//        System.out.println(response.getBody());


//        return response.hasBody() ? "1": "0";



        String response = restTemplate.postForObject(functionUrl, headers, String.class);
                System.out.println(response);
        return response;
    }


    @GetMapping("test_invoice")
    public String test_invoice(){


        return "pages/test_invoice";
    }
}
