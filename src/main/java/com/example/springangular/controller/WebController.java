package com.example.springangular.controller;

import com.example.springangular.config.CspNonceFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RestController
public class WebController {

    @GetMapping(value = {"/", "/app", "/app/**"}, produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> index(HttpServletRequest request) throws IOException {
        String nonce = String.valueOf(request.getAttribute(CspNonceFilter.NONCE_ATTR));
        String html = loadIndexHtml();

        String withMeta = html.replace("</head>",
                "  <meta name=\"csp-nonce\" content=\"" + nonce + "\">\n" +
                        "  <script nonce=\"" + nonce + "\">\n" +
                        "    window.__CSP_NONCE__ = '" + nonce + "';\n" +
                        "    const originalFetch = window.fetch;\n" +
                        "    window.fetch = (input, init = {}) => {\n" +
                        "      const headers = new Headers(init.headers || {});\n" +
                        "      headers.set('X-CSP-Nonce', window.__CSP_NONCE__);\n" +
                        "      return originalFetch(input, { ...init, headers });\n" +
                        "    };\n" +
                        "  </script>\n</head>");

        String withScriptNonces = withMeta.replaceAll("<script(?![^>]*\\bnonce=)", "<script nonce=\"" + nonce + "\"");
        return ResponseEntity.ok(withScriptNonces);
    }

    private String loadIndexHtml() throws IOException {
        ClassPathResource staticIndex = new ClassPathResource("static/index.html");
        if (staticIndex.exists()) {
            return StreamUtils.copyToString(staticIndex.getInputStream(), StandardCharsets.UTF_8);
        }
        ClassPathResource fallbackIndex = new ClassPathResource("public/index.html");
        if (fallbackIndex.exists()) {
            return StreamUtils.copyToString(fallbackIndex.getInputStream(), StandardCharsets.UTF_8);
        }
        return "<html><body><h1>Angular build not found</h1><p>Run frontend build first.</p></body></html>";
    }
}
