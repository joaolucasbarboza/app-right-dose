package com.fema.tcc.config.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import java.io.IOException;
import java.io.InputStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
public class FirebaseConfig {

  @Value("${firebase.service-account-file}")
  private Resource serviceAccountResource;

  @Bean
  public FirebaseApp firebaseApp() throws IOException {
    if (!FirebaseApp.getApps().isEmpty()) {
      return FirebaseApp.getInstance();
    }

    try (InputStream is = serviceAccountResource.getInputStream()) {
      FirebaseOptions options =
          FirebaseOptions.builder().setCredentials(GoogleCredentials.fromStream(is)).build();

      return FirebaseApp.initializeApp(options);
    }
  }

  @Bean
  public FirebaseMessaging firebaseMessaging(FirebaseApp firebaseApp) {
    return FirebaseMessaging.getInstance(firebaseApp);
  }
}
