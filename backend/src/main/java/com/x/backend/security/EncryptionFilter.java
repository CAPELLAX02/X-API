package com.x.backend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

public class EncryptionFilter extends OncePerRequestFilter {

    private final SecretKey secretKey;
    private final IvParameterSpec ivSpec;

    public EncryptionFilter(SecretKey secretKey, IvParameterSpec ivSpec) {
        this.secretKey = secretKey;
        this.ivSpec = ivSpec;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        // Wrap the response to capture plaintext output
        CharResponseWrapper responseWrapper = new CharResponseWrapper(response);
        filterChain.doFilter(request, responseWrapper);

        // Get the plaintext response
        String originalResponse = responseWrapper.toString();

        // Encrypt the response using AESUtil
        String encryptedResponse;
        try {
            encryptedResponse = AESUtil.encrypt(originalResponse);
        } catch (Exception e) {
            throw new ServletException("Failed to encrypt response body.", e);
        }

        // Set content type and content length (optional)
        response.setContentType("text/plain;charset=UTF-8");
        byte[] encryptedBytes = encryptedResponse.getBytes(StandardCharsets.UTF_8);
        response.setContentLength(encryptedBytes.length);

        // Write the encrypted response using getWriter()
        PrintWriter writer = response.getWriter();
        writer.write(encryptedResponse);
        writer.flush();
    }
}
