package org.binar.pragosacademyapi.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;

@Component
public class FirebaseConfig {
    @Autowired
    ResourceLoader resourceLoader;
    @PostConstruct
    public void initialization() throws IOException {
        Resource resource = resourceLoader.getResource("classpath:firebase-admin.json");
        InputStream inputStream = resource.getInputStream();

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(inputStream))
                .setDatabaseUrl("https://pragos-academy-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .build();

        FirebaseApp.initializeApp(options);

    }
}
