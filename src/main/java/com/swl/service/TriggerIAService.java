package com.swl.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.swl.models.project.Requirement;
import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TriggerIAService {

    @Value("${swl.ia.url}")
    private String urlApi;


    public void classifyRequirement(Requirement requirement) {

        ObjectMapper mapper = new ObjectMapper();
        String requestBody = null;
        try {
            requestBody = mapper
                    .writerWithDefaultPrettyPrinter()
                    .writeValueAsString(requirement);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlApi))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpClient httpClient = HttpClient.newBuilder().build();
        JSONObject jsonObject = null;

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            jsonObject = new JSONObject(response.body());
        } catch (IOException | InterruptedException | JSONException e) {
            e.printStackTrace();
        }

        if (!Objects.isNull(jsonObject)) {
            try {
                requirement.setCategory(jsonObject.getString("category"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
