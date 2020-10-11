package com.example.productdownloader;

import org.json.JSONObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedWriter;
import java.io.FileWriter;


@SpringBootApplication
public class ProductdownloaderApplication {

    private static String url = "https://api.stuller.com/v2/products/";

    public static void main(String[] args) throws Exception{
        SpringApplication.run(ProductdownloaderApplication.class, args);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
        RestTemplate restTemplate = restTemplateBuilder.basicAuthentication("jandjdev","Welcome1").build();

        String response = restTemplate.getForObject(url,String.class);

        String filename = "products\\dump";
        String nextPage = getNextPage(response);
        int ctr = 1;
        while(nextPage!= null) {

            // write to file
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename + ctr + ".json"));
            writer.write(response);

            writer.close();

            System.out.println(ctr++ + " == "+nextPage);
            JSONObject request = new JSONObject();
            request.put("NextPage", nextPage);

            HttpEntity<String> entity = new HttpEntity<String>(request.toString(), headers);
            response = restTemplate.postForEntity(url, entity, String.class).getBody();



            nextPage = getNextPage(response);
        }
    }


    private static String getNextPage(String response) throws Exception{
        return new JSONObject(response).getString("NextPage");
    }

}
