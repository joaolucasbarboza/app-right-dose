package com.fema.tcc.gateways.http.jsons;

import java.util.List;

public record OllamaChatRequest(String model, List<OllamaMessage> messages, boolean stream) {}
