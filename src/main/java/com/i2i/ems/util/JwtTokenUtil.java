package com.i2i.ems.util;

import com.i2i.ems.helper.UnAuthorizedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtTokenUtil {

  private static String key = "";

  public JwtTokenUtil() throws NoSuchAlgorithmException {
    KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
    SecretKey secretKey = keyGenerator.generateKey();
    key = Base64.getEncoder().encodeToString(secretKey.getEncoded());
  }

  public boolean validate(String token) {
    try {
      Jwts.parser()
          .verifyWith(getKey())
          .build()
          .parse(token);
      isTokenExpired(token);
      return true;
    } catch (UnAuthorizedException e) {
      throw new UnAuthorizedException("Token expired");
    } catch (Exception e) {
      throw new UnAuthorizedException("Invalid token");
    }
  }

  public String generateAccessToken(String username) {
    Map<String, Object> claims = new HashMap<>();

    return Jwts.builder()
        .claims(claims)
        .subject(username)
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(new Date(System.currentTimeMillis() + 3600000))
        .signWith(getKey())
        .compact();
  }

  private SecretKey getKey(){
    return Keys.hmacShaKeyFor(Decoders.BASE64.decode(key));
  }

  public String getUsername(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  private void isTokenExpired(String token) {
    if(extractClaim(token, Claims::getExpiration).before(new Date())) {
      throw new UnAuthorizedException("Token expired");
    }
  }

  private Claims extractAllClaims(String token) {
    return Jwts
        .parser()
        .verifyWith(getKey())
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }
}