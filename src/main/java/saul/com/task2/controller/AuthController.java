package saul.com.task2.controller;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import saul.com.task2.constant.Constants;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> loginRequest){
        String username = loginRequest.get("username");
        String password = loginRequest.get("password");

        if (validateCredentials(username, password)){
           String token = generateToken(username);
           Map<String, String> response = new HashMap<>();
           response.put("token", token);
           return response;
        }
        else
            throw new RuntimeException("Invalid Credentials Provided");
    }

    private static boolean validateCredentials(String username, String password) {
        return username.equals("admin") && password.equals("password");
    }

    private String generateToken(String username) {
        SecretKey secretKey = new SecretKeySpec(Constants.SECRET_KEY.getBytes(), "HmacSHA256");

        return Jwts.builder()
                .setSubject(username)
                .claim("roles", "ROLE_ADMIN")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 10000 * 60)) // 1 minute expiration
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }
}
