package com.i2i.ems.util;

import com.i2i.ems.helper.UnAuthorizedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
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

/**
 * <p>
 * Utility class to generate and validate JWT token.
 * </p>
 */
@Component
public class JwtTokenUtil {

  private static String key = "";

  /**
   * <p>
   * Constructor to generate secret key.
   * </p>
   */
  public JwtTokenUtil() throws NoSuchAlgorithmException {
    KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
    SecretKey secretKey = keyGenerator.generateKey();
    key = Base64.getEncoder().encodeToString(secretKey.getEncoded());
  }

  /**
   * <p>
   * Validates the token.
   * </p>
   *
   * @param token Token to be validated.
   * @return {@link Boolean} True if token is valid, false otherwise.
   * @throws UnAuthorizedException If token is invalid.
   * @throws UnAuthorizedException If token is expired.
   */
  public boolean validate(String token) {
    try {
      Jwts.parser()
          .verifyWith(getKey())
          .build()
          .parse(token);
      return true;
    } catch (ExpiredJwtException e) {
      throw new UnAuthorizedException("Token expired");
    } catch (UnAuthorizedException e) {
      throw new UnAuthorizedException(e.getMessage());
    } catch (Exception e) {
      throw new UnAuthorizedException("Invalid token");
    }
  }

  /**
   * <p>
   * Generates access token.
   * </p>
   *
   * @param username Username to generate token.
   * @return {@link String} Generated token.
   */
  public String generateAccessToken(String username) {
    Map<String, Object> claims = new HashMap<>();

    return Jwts.builder()
        .claims(claims)
        .subject(username)
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(new Date(System.currentTimeMillis() + 60 * 60 * 10))
        .signWith(getKey())
        .compact();
  }

  /**
   * <p>
   *   Creates a secret key from the key string.
   *   Uses HmacSHA256 algorithm.
   * </p>
   *
   * @return {@link SecretKey} Secret key generated from the key string.
   */
  private SecretKey getKey(){
    return Keys.hmacShaKeyFor(Decoders.BASE64.decode(key));
  }

  /**
   * <p>
   * Extracts username from the token.
   * </p>
   *
   * @param token Token from which username is to be extracted.
   * @return {@link String} Username extracted from the token.
   */
  public String getUsername(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  /**
   * <p>
   * Extracts claim from the token.
   * </p>
   *
   * @param token Token from which claim is to be extracted.
   * @param claimsResolver Function to extract claim.
   * @return {@link T} Claim extracted from the token.
   */
  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  /**
   * <p>
   * Checks if the token is expired.
   * </p>
   *
   * @param token Token to be checked.
   * @throws UnAuthorizedException If token is expired.
   */
  private void isTokenExpired(String token) throws UnAuthorizedException {
    if(extractClaim(token, Claims::getExpiration).before(new Date())) {
      throw new UnAuthorizedException("Token expired");
    }
  }

  /**
   * <p>
   * Extracts all claims from the token.
   * </p>
   *
   * @param token Token from which claims are to be extracted.
   * @return {@link Claims} All claims extracted from the token.
   */
  private Claims extractAllClaims(String token) {
    return Jwts
        .parser()
        .verifyWith(getKey())
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }
}