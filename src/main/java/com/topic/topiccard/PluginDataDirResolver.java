package com.topic.topiccard;

import run.halo.app.plugin.PluginContext;

import java.io.File;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.nio.file.Paths;

final class PluginDataDirResolver {

    private PluginDataDirResolver() {}

    static Path resolve(PluginContext context) {
        // Try common method names on PluginContext via reflection to avoid compile errors across versions
        String[] candidates = new String[] {
                "getDataFolder", "getDataDir", "getDataPath", "getWorkDir", "getPluginHome", "getPluginPath"
        };
        for (String name : candidates) {
            try {
                Method m = context.getClass().getMethod(name);
                Object result = m.invoke(context);
                if (result instanceof Path) {
                    return (Path) result;
                }
                if (result instanceof File) {
                    return ((File) result).toPath();
                }
                if (result != null) {
                    // Fallback: if string-like
                    return Paths.get(result.toString());
                }
            } catch (Throwable ignored) {
                // try next
            }
        }
        // Final fallback: user home under .halo/plugin-data/topic-card
        String userHome = System.getProperty("user.home", ".");
        return Paths.get(userHome, ".halo", "plugin-data", "topic-card");
    }
}


