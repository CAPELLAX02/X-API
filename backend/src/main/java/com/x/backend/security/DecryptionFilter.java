package com.x.backend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;

public class DecryptionFilter extends OncePerRequestFilter {

    private final SecretKey secretKey;
    private final IvParameterSpec ivSpec;

    public DecryptionFilter(SecretKey secretKey, IvParameterSpec ivSpec) {
        this.secretKey = secretKey;
        this.ivSpec = ivSpec;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        BufferedReader originalReader = new BufferedReader(
                new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8)
        );
        String encryptedBody = originalReader.lines().collect(Collectors.joining());

        String decryptedBody;
        try {
            decryptedBody = AESUtil.decrypt(encryptedBody);
        } catch (Exception e) {
            throw new ServletException("Failed to decrypt request body.", e);
        }

        byte[] decryptedBytes = decryptedBody.getBytes(StandardCharsets.UTF_8);

        HttpServletRequestWrapper requestWrapper = new HttpServletRequestWrapper(request) {

            @Override
            public String getContentType() {
                return "application/json";
            }

            @Override
            public String getHeader(String name) {
                if ("Content-Type".equalsIgnoreCase(name)) {
                    return "application/json";
                }
                return super.getHeader(name);
            }

            @Override
            public Enumeration<String> getHeaders(String name) {
                if ("Content-Type".equalsIgnoreCase(name)) {
                    return Collections.enumeration(List.of("application/json"));
                }
                return super.getHeaders(name);
            }

            @Override
            public BufferedReader getReader() {
                return new BufferedReader(
                        new InputStreamReader(new ByteArrayInputStream(decryptedBytes), StandardCharsets.UTF_8)
                );
            }

            @Override
            public ServletInputStream getInputStream() {
                return new ServletInputStream() {
                    private final ByteArrayInputStream bais = new ByteArrayInputStream(decryptedBytes);
                    @Override
                    public int read() throws IOException {
                        return bais.read();
                    }
                    @Override
                    public boolean isFinished() {
                        return bais.available() <= 0;
                    }
                    @Override
                    public boolean isReady() {
                        return true;
                    }
                    @Override
                    public void setReadListener(ReadListener readListener) {
                        // Not implemented
                    }
                };
            }
        };

        filterChain.doFilter(requestWrapper, response);
    }
}
