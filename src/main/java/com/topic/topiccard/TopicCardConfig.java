package com.topic.topiccard;

import java.util.ArrayList;
import java.util.List;

public class TopicCardConfig {
    public static class Item {
        public String title;
        public String imageUrl;
        public String link;
        public int articleCount;
    }

    public List<Item> items = new ArrayList<>();
}


