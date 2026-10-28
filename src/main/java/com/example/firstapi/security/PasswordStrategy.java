package com.example.firstapi.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import java.security.MessageDigest;

@Component
public class PasswordStrategy {

    @Value("${app.security.password-algorithm:MD5}")
    private String algorithm;

    public String encode(String rawPassword) {
        if ("BCRYPT".equalsIgnoreCase(algorithm)) {
            return new BCryptPasswordEncoder().encode(rawPassword);
        } else {
            // 默认 MD5
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                byte[] digest = md.digest(rawPassword.getBytes());
                StringBuilder sb = new StringBuilder();
                for (byte b : digest) sb.append(String.format("%02x", b));
                return sb.toString();
            } catch (Exception e) {
                throw new RuntimeException("加密失败", e);
            }
        }
    }

    // 密码匹配（BCrypt 不能直接比对字符串，必须用它的 matches 方法）
    public boolean matches(String rawPassword, String encodedPassword) {
        if ("BCRYPT".equalsIgnoreCase(algorithm)) {
            return new BCryptPasswordEncoder().matches(rawPassword, encodedPassword);
        } else {
            return encode(rawPassword).equals(encodedPassword);
        }
    }
}
