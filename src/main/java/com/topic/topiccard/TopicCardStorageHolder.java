package com.topic.topiccard;

import java.nio.file.Path;

public final class TopicCardStorageHolder {
    private static Path dataDirectory;

    private TopicCardStorageHolder() {
    }

    public static void setDataDirectory(Path dir) {
        dataDirectory = dir;
    }

    public static Path getDataDirectory() {
        return dataDirectory;
    }
}


