package net.azisaba.breakdrop.config;

import net.azisaba.breakdrop.util.ConfigUtil;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public final class PluginConfig {
    private final List<ConfigDrop> drops;

    public PluginConfig(@NotNull List<@NotNull ConfigDrop> drops) {
        this.drops = drops;
    }

    @Contract(pure = true)
    public static @NotNull PluginConfig load(@NotNull ConfigurationSection section) {
        List<ConfigDrop> drops =
                Objects.requireNonNull(section.getList("drops"))
                        .stream()
                        .map(ConfigUtil::toConfigurationSection)
                        .map(ConfigDrop::load)
                        .collect(Collectors.toList());
        return new PluginConfig(drops);
    }

    @Contract(pure = true)
    public @NotNull List<@NotNull ConfigDrop> getDrops() {
        return drops;
    }
}
