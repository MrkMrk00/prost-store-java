package de.unibamberg.dsam.group6.bifunction;

import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;

import java.io.BufferedWriter;

public class Statistics implements HttpFunction {

    @Override
    public void service(HttpRequest request, HttpResponse response) throws Exception {
        BufferedWriter writer = response.getWriter();
        writer.write("Hello, World from BI function!");
    }
}
