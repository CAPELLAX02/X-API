package com.x.backend.security;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

public class CharResponseWrapper extends HttpServletResponseWrapper {

    private final ByteArrayOutputStream output;
    private PrintWriter writer;

    public CharResponseWrapper(HttpServletResponse response) {
        super(response);
        output = new ByteArrayOutputStream();
    }

    @Override
    public ServletOutputStream getOutputStream() {
        return new ServletOutputStream() {
            @Override
            public void write(int b) throws IOException {
                output.write(b);
            }
            @Override
            public boolean isReady() {
                return true;
            }
            @Override
            public void setWriteListener(WriteListener writeListener) {
                // Optional: implement if asynchronous I/O is needed.
            }
        };
    }

    @Override
    public PrintWriter getWriter() {
        if (writer == null) {
            writer = new PrintWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8));
        }
        return writer;
    }

    @Override
    public void flushBuffer() throws IOException {
        if (writer != null) {
            writer.flush();
        }
        output.flush();
    }

    @Override
    public String toString() {
        try {
            flushBuffer();
        } catch (IOException e) {
            // Log error if needed
        }
        return output.toString(StandardCharsets.UTF_8);
    }
}
