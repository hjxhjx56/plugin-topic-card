package com.topic.topiccard;

import org.springframework.stereotype.Component;
import run.halo.app.plugin.BasePlugin;
import run.halo.app.plugin.PluginContext;

/**
 * <p>Plugin main class to manage the lifecycle of the plugin.</p>
 * <p>This class must be public and have a public constructor.</p>
 * <p>Only one main class extending {@link BasePlugin} is allowed per plugin.</p>
 *
 * @author hongjiaxin
 * @since 1.0.0
 */
@Component
public class TopicCardPlugin extends BasePlugin {

    private final PluginContext pluginContext;

    public TopicCardPlugin(PluginContext pluginContext) {
        super(pluginContext);
        this.pluginContext = pluginContext;
    }

    @Override
    public void start() {
        // 暴露数据目录，供存储服务使用
        TopicCardStorageHolder.setDataDirectory(PluginDataDirResolver.resolve(pluginContext));
        System.out.println("插件启动成功！");
    }

    @Override
    public void stop() {
        System.out.println("插件停止！");
    }
}
