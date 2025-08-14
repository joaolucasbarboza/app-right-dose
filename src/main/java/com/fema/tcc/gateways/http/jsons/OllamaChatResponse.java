package com.fema.tcc.gateways.http.jsons;

import java.util.Map;

public record OllamaChatResponse(String model, Map<String, Object> message, boolean done) {}
