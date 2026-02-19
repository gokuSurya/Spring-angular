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

        String withNonceMeta = upsertNonceMeta(html, nonce);
        String withScriptNonces = withNonceMeta.replaceAll("<script(?![^>]*\\bnonce=)", "<script nonce=\"" + nonce + "\"");

        return ResponseEntity.ok(withScriptNonces);
    }

    private String upsertNonceMeta(String html, String nonce) {
        String metaTag = "<meta name=\"csp-nonce\" content=\"" + nonce + "\">";

        if (html.contains("name=\"csp-nonce\"")) {
            return html.replaceAll("<meta\\s+name=\"csp-nonce\"\\s+content=\"[^\"]*\"\\s*/?>", metaTag);
        }

        return html.replace("</head>", "  " + metaTag + "\n</head>");
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
