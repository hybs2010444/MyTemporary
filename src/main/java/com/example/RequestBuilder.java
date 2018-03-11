package com.example;

public class RequestBuilder {

	public static Request build(String type, String content) {
		Request request = new Request();

		Image image = new Image();
		image.setContent(content);
		request.setImage(image);

		Feature feature = new Feature();
		feature.setType(type);

		Feature[] features = new Feature[1];
		features[0] = feature;
		request.setFeatures(features);

		return request;
	}

}
