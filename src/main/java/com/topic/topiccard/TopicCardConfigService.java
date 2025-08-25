package com.topic.topiccard;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
//import java.util.Objects;

public class TopicCardConfigService {
    private static final String FILE_NAME = "config.json";
    private final ObjectMapper objectMapper = new ObjectMapper();

    public synchronized TopicCardConfig read() {
        Path dir = TopicCardStorageHolder.getDataDirectory();
        // 在主题预览或插件尚未完全启动时，数据目录可能为 null，此时优雅降级为默认配置，避免中断页面渲染
        if (dir == null) {
            return createDefaultConfig();
        }

        Path file = dir.resolve(FILE_NAME);
        if (!Files.exists(file)) {
            TopicCardConfig cfg = createDefaultConfig();
            // 尝试写入默认配置（如果目录可用）
            write(cfg);
            return cfg;
        }
        try {
            byte[] bytes = Files.readAllBytes(file);
            return objectMapper.readValue(new String(bytes, StandardCharsets.UTF_8), TopicCardConfig.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read config", e);
        }
    }

    public synchronized void write(TopicCardConfig config) {
        try {
            Path dir = TopicCardStorageHolder.getDataDirectory();
            // 在预览模式下可能为 null，此时跳过持久化但不抛异常
            if (dir == null) {
                return;
            }
            Files.createDirectories(dir);
            Path file = dir.resolve(FILE_NAME);
            byte[] bytes = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(config).getBytes(StandardCharsets.UTF_8);
            Files.write(file, bytes);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write config", e);
        }
    }

    private static TopicCardConfig createDefaultConfig() {
        TopicCardConfig cfg = new TopicCardConfig();
        TopicCardConfig.Item item = new TopicCardConfig.Item();
        item.title = "热门专题";
        item.imageUrl = "/apis/topic-card/v1/logo";
        item.link = "#";
        item.articleCount = 0;
        cfg.items.add(item);
        return cfg;
    }
}


