package com.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class GCPTrial {

	private static String API_KEY="My api key string";

	public static void main(String args[]) {
		String imagePath;
		try {
			imagePath = args[0];
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("ERROR:Input image file");
			return;
		}

		String encoded;
		try {
			encoded = readImageFile(imagePath);
		} catch (IOException e) {
			System.out.println("ERROR:Read image file");
			return;
		}

		Request request = RequestBuilder.build("TEXT_DETECTION", encoded);
		Request[] requests = new Request[1];
		requests[0] = request;

		RequestBody body = new RequestBody();
		body.setRequests(requests);

		ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
		String json;
		try {
			json = objectMapper.writeValueAsString(body);
		} catch (JsonProcessingException e) {
			System.out.println("ERROR:Json processing failed");
			return ;
		}
		// System.out.println(json);

		Client client = ClientBuilder.newClient();
		String result;
		try {
			result = client.target("https://vision.googleapis.com")
					.path("/v1/images:annotate")
					.queryParam("key", API_KEY)
					.request()
					.post(Entity.entity(json, MediaType.APPLICATION_JSON), String.class);
		} catch (Exception e) {
			System.out.println("ERROR:HTTP request failed");
			e.printStackTrace();
			return;
		}
		System.out.println(result);
	}

	private static String readImageFile(String imagePath) throws IOException {
		byte[] fileContentBytes;
		fileContentBytes = Files.readAllBytes(Paths.get(imagePath));
		String encoded = Base64.getEncoder().encodeToString(fileContentBytes);
		// System.out.println("encoded:" + encoded);
		return encoded;
	}
}
