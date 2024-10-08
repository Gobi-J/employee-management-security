package com.i2i.ems.config;

import com.i2i.ems.helper.UnAuthorizedException;
import com.i2i.ems.service.UserService;
import com.i2i.ems.util.JwtTokenUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

/**
 * <p>
 * Filter to validate jwt token and set user identity on spring security context.
 * </p>
 */
public class JwtTokenFilter extends OncePerRequestFilter {

  private final HandlerExceptionResolver handlerExceptionResolver;
  @Autowired
  private JwtTokenUtil jwtTokenUtil;
  @Autowired
  private UserService userService;

  public JwtTokenFilter(HandlerExceptionResolver handlerExceptionResolver) {
    this.handlerExceptionResolver = handlerExceptionResolver;
  }

  /**
   * <p>
   * Filter to validate jwt token and set user identity on spring security context.
   * </p>
   *
   * @param request  HttpServletRequest object.
   * @param response HttpServletResponse object.
   * @param chain    FilterChain object.
   * @throws ServletException Exception thrown by the servlet.
   * @throws IOException      Exception thrown by the input/output operation.
   */
  @Override
  public void doFilterInternal(HttpServletRequest request,
                               HttpServletResponse response,
                               FilterChain chain)
      throws ServletException, IOException {

    if (request.getRequestURI().contains("/v1/auth/")) {
      chain.doFilter(request, response);
      return;
    }
    try {
      final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
      if (header == null || !header.startsWith("Bearer ")) {
        throw new UnAuthorizedException("Invalid Authorization header");
      }

      final String token = header.split(" ")[1].trim();
      if (!jwtTokenUtil.validate(token)) {
        throw new UnAuthorizedException("Invalid token");
      }

      UserDetails userDetails = userService.loadUserByUsername(jwtTokenUtil.getUsername(token));

      if (userDetails == null) {
        chain.doFilter(request, response);
        return;
      }

      UsernamePasswordAuthenticationToken
          authentication = new UsernamePasswordAuthenticationToken(
          userDetails, null,
          userDetails.getAuthorities()
      );

      authentication.setDetails(
          new WebAuthenticationDetailsSource().buildDetails(request)
      );

      SecurityContextHolder.getContext().setAuthentication(authentication);
      chain.doFilter(request, response);
    } catch (UnAuthorizedException e) {
      handlerExceptionResolver.resolveException(request, response, null, e);
    }
  }
}