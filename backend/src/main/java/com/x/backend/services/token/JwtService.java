package com.x.backend.services.token;

import com.x.backend.models.user.user.ApplicationUser;
import io.jsonwebtoken.Claims;

import java.util.List;
import java.util.function.Function;

/**
 * Service interface for managing JSON Web Tokens (JWT) used in authentication and authorization.
 * <p>
 * Provides utility methods for creating, validating, and extracting information from JWTs,
 * including both access and refresh tokens. This service leverages RSA-based asymmetric encryption
 * to ensure secure signing and verification of tokens.
 * </p>
 */
public interface JwtService {

    /**
     * Generates a short-lived access token for the given user.
     *
     * @param user the authenticated user for whom the token is generated
     * @return a signed JWT string containing user claims and valid for 15 minutes
     */
    String generateAccessToken(ApplicationUser user);

    /**
     * Generates a long-lived refresh token for the given user.
     *
     * @param user the authenticated user for whom the refresh token is generated
     * @return a signed JWT string used to refresh expired access tokens, valid for 7 days
     */
    String generateRefreshToken(ApplicationUser user);

    /**
     * Extracts the JWT ID (jti claim) from a given token.
     *
     * @param token the JWT string
     * @return the unique identifier (JTI) of the token
     */
    String extractJtiFromToken(String token);

    /**
     * Extracts the username (subject claim) from a JWT.
     *
     * @param token the JWT string
     * @return the username embedded in the token
     */
    String extractUsernameFromToken(String token);

    /**
     * Extracts the user ID (custom "uid" claim) from the token.
     *
     * @param token the JWT string
     * @return the user ID embedded in the token
     */
    Long extractUserIdFromToken(String token);

    /**
     * Extracts the list of role authorities from the JWT (custom "roles" claim).
     *
     * @param token the JWT string
     * @return list of roles (e.g., ROLE_USER, ROLE_ADMIN) assigned to the user
     */
    List<String> extractRoles(String token);

    /**
     * Extracts a specific claim from a JWT using a resolver function.
     *
     * @param token           the JWT string
     * @param claimsResolver  function that defines which claim to extract
     * @param <T>             the type of the claim
     * @return the extracted claim
     */
    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);

    /**
     * Validates whether a given JWT is valid and belongs to the specified user.
     *
     * @param token the JWT string
     * @param user  the expected user
     * @return true if the token is valid and corresponds to the user, false otherwise
     */
    boolean isTokenValid(String token, ApplicationUser user);

    /**
     * Checks whether a token is expired based on its expiration claim.
     *
     * @param token the JWT string
     * @return true if the token is expired, false otherwise
     */
    boolean isTokenExpired(String token);

    /**
     * Retrieves the remaining expiration time of the token in seconds.
     *
     * @param token the JWT string
     * @return seconds until token expires from current system time
     */
    long getExpirationFromToken(String token);

}
