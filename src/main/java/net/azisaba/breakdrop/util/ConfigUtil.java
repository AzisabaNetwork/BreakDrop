package net.azisaba.breakdrop.util;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class ConfigUtil {
    @Contract("null -> null")
    public static ConfigurationSection toConfigurationSection(@Nullable Object o) {
        if (o == null) {
            return null;
        }
        if (o instanceof ConfigurationSection) {
            return (ConfigurationSection) o;
        }
        if (o instanceof Map<?, ?>) {
            Map<?, ?> map = (Map<?, ?>) o;
            ConfigurationSection section = new MemoryConfiguration();
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                if (entry.getKey() instanceof String) {
                    section.set((String) entry.getKey(), entry.getValue());
                }
            }
            return section;
        }
        return new MemoryConfiguration();
    }
}
