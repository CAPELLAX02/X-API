package com.x.backend.security.ratelimit;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Servlet filter that limits the number of requests a client can make within a given timeframe.
 * <p>
 * This filter uses Bucket4j to enforce IP-based rate limiting with a token bucket strategy.
 */
@Component
public class RateLimitingFilter extends OncePerRequestFilter {

    private static final long REQUEST_LIMIT = 100;
    private static final Duration REQUEST_LIMIT_DURATION = Duration.ofMinutes(1);

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    /**
     * Creates a new token bucket with the specified rate limit.
     */
    private Bucket createNewBucket() {
        return Bucket.builder()
                .addLimit(Bandwidth.classic(REQUEST_LIMIT, Refill.greedy(REQUEST_LIMIT, REQUEST_LIMIT_DURATION)))
                .build();
    }

    /**
     * Extracts a client identifier (IP address) from the request.
     */
    private String resolveClientKey(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        if (forwarded != null)
            return forwarded.split(",")[0];
        else
            return request.getRemoteAddr();
    }

    /**
     * Filters incoming requests and applies rate limiting.
     * Returns HTTP 429 if the client exceeds the allowed number of requests.
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException
    {
        String clientKey = resolveClientKey(request);
        Bucket bucket = buckets.computeIfAbsent(clientKey, k -> createNewBucket());

        if (bucket.tryConsume(1)) {
            filterChain.doFilter(request, response);
        }
        else {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.getWriter().write("Too many requests – Rate limit exceeded. Try again later.");
        }
    }

}
