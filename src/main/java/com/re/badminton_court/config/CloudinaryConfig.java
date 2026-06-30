package com.re.badminton_court.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {
    @Value("${cloudinary.cloud-name}")
    private String cloudName;

    @Value("${cloudinary.api-key}")
    private String apiKey;

    @Value("${cloudinary.api-secret}")
    private String apiSecret;

    @Value("${cloudinary.connection-timeout:60000}")
    private String connectionTimeout;

    @Value("${cloudinary.read-timeout:60000}")
    private String readTimeout;

    @Bean
    public Cloudinary cloudinary() {
        Map<String, Object> config = new HashMap<>(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret,
                "secure", true
        ));
        config.put("timeout", Long.parseLong(connectionTimeout));
        config.put("read_timeout", Long.parseLong(readTimeout));
        return new Cloudinary(config);
    }
}
