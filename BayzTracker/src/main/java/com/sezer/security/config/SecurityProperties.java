package com.sezer.security.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component @Data @NoArgsConstructor @AllArgsConstructor
public class SecurityProperties {
    @Value("${apt.jwt.secret}")
    public String secretKey;
    @Value("${apt.jwt.token.prefix}")
    public String tokenPrefix;
    @Value("${apt.jwt.token.expireTime}")
    public long expireTime;
}
