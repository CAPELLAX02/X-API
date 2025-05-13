package com.x.backend.services.token.filter;

import com.x.backend.models.user.user.ApplicationUser;
import com.x.backend.repositories.ValidAccessTokenRepository;
import com.x.backend.services.token.JwtService;
import com.x.backend.services.user.UserServiceImpl;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserServiceImpl userServiceImpl;
    private final ValidAccessTokenRepository validAccessTokenRepository;

    public JwtAuthenticationFilter(@Qualifier("jwtService") JwtService jwtService,
                                   UserServiceImpl userServiceImpl,
                                   ValidAccessTokenRepository validAccessTokenRepository)
    {
        this.jwtService = jwtService;
        this.userServiceImpl = userServiceImpl;
        this.validAccessTokenRepository = validAccessTokenRepository;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException
    {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        String token = authorizationHeader.substring(7);

        try {
            String tokenType = jwtService.extractClaim(token, claims -> claims.get("token_type", String.class));

            if (!"access".equals(tokenType)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            String jti = jwtService.extractJtiFromToken(token);
            if (!validAccessTokenRepository.existsByJti(jti)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            String username = jwtService.extractUsernameFromToken(token);
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                ApplicationUser user = userServiceImpl.getUserByUsername(username);

                if (jwtService.isTokenValid(token, user)) {
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(
                                    user,
                                    null,
                                    user.getAuthorities()
                            );
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
            filterChain.doFilter(request, response);
        }
        catch (JwtException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

}
