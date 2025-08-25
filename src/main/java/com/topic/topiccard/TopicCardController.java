package com.topic.topiccard;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;
import java.util.Objects;

@RestController
@RequestMapping("/apis/topic-card/v1")
public class TopicCardController {

    private final TopicCardConfigService service = new TopicCardConfigService();

    @PreAuthorize("permitAll()")
    @GetMapping(path = "/items", produces = MediaType.APPLICATION_JSON_VALUE)
    public TopicCardConfig getItems() {
        return service.read();
    }

    @PostMapping(path = "/items", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void saveItems(@RequestBody TopicCardConfig config) {
        service.write(config);
    }

    @PreAuthorize("permitAll()")
    @GetMapping(path = "/fragment", produces = MediaType.TEXT_HTML_VALUE)
    public String getFragmentHtml() {
        TopicCardConfig cfg = service.read();
        StringBuilder sb = new StringBuilder();
        sb.append("<div class=\"topic-grid\">");
        for (TopicCardConfig.Item item : cfg.items) {
            String image = escapeHtml(item.imageUrl);
            String title = escapeHtml(item.title);
            String link = escapeHtml(item.link);
            int count = item.articleCount;
            sb.append("<div class=\"topic-card\" onclick=\"if('" + link + "'&&'" + link + "'!=='#')location.href='" + link + "'\">");
            sb.append("<div class=\"card-image-wrapper\">");
            sb.append("<img class=\"card-image\" src=\"" + image + "\" alt=\"" + title + "\" onerror=\"this.src='/apis/topic-card/v1/logo'\"/>");
            sb.append("<div class=\"image-overlay\"></div>");
            sb.append("</div>");
            sb.append("<div class=\"card-content\">");
            sb.append("<h3 class=\"card-title\">" + title + "</h3>");
            if (count > 0) {
                sb.append("<span class=\"article-count\">" + count + " 篇</span>");
            }
            sb.append("</div>");
            sb.append("</div>");
        }
        sb.append("</div>");
        return sb.toString();
    }

    @PreAuthorize("permitAll()")
    @GetMapping(path = "/logo", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getLogoImage() {
        // 使用当前类的类加载器读取资源，避免在某些运行环境中线程上下文类加载器不可见导致 404
        InputStream inputStream = TopicCardController.class.getResourceAsStream("/logo.png");
        if (Objects.isNull(inputStream)) {
            return ResponseEntity.notFound().build();
        }
        try (InputStream is = inputStream) {
            byte[] bytes = is.readAllBytes();
            return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(bytes);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    private static String escapeHtml(String input) {
        if (input == null) return "";
        return input.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}


