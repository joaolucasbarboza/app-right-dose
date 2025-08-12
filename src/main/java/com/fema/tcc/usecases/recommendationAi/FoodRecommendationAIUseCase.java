package com.fema.tcc.usecases.recommendationAi;

import com.openai.client.OpenAIClient;
import com.openai.models.ChatModel;
import com.openai.models.responses.Response;
import com.openai.models.responses.ResponseCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FoodRecommendationAIUseCase {

  private final OpenAIClient openAIClient;

  private static final ChatModel MODEL = ChatModel.GPT_4_1_NANO;

  public String simpleResponse() {
    ResponseCreateParams params =
        ResponseCreateParams.builder()
            .model(MODEL)
            .input("Escreva uma frase simples dizendo olá.")
            .build();

    Response resp = openAIClient.responses().create(params);
    return resp.output().toString();
  }
}
