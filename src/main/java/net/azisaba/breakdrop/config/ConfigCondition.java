package net.azisaba.breakdrop.config;

import net.azisaba.breakdrop.util.ConfigUtil;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public final class ConfigCondition implements Condition {
    private final List<ConfigSlotItem> inventory;
    private final Set<String> worlds;
    private final Set<String> worldsExcept;
    private final @Nullable Integer maxYInclusive;
    private final @Nullable Integer minYInclusive;
    private final List<ConfigVariableCondition> variables;

    public ConfigCondition(
            @NotNull List<@NotNull ConfigSlotItem> inventory,
            @NotNull Set<@NotNull String> worlds,
            @NotNull Set<@NotNull String> worldsExcept,
            @Nullable Integer maxYInclusive,
            @Nullable Integer minYInclusive,
            @NotNull List<@NotNull ConfigVariableCondition> variables
    ) {
        this.inventory = inventory;
        this.worlds = worlds;
        this.worldsExcept = worldsExcept;
        this.maxYInclusive = maxYInclusive;
        this.minYInclusive = minYInclusive;
        this.variables = variables;
    }

    @Contract(value = "_ -> new", pure = true)
    public static @NotNull ConfigCondition load(@NotNull ConfigurationSection section) {
        List<ConfigSlotItem> inventory =
                Objects.requireNonNull(section.getList("inventory"))
                        .stream()
                        .map(ConfigUtil::toConfigurationSection)
                        .map(ConfigSlotItem::load)
                        .collect(Collectors.toList());
        Set<String> worlds = new HashSet<>(section.getStringList("worlds"));
        Set<String> worldsExcept = new HashSet<>(section.getStringList("worlds_except"));
        Integer maxYInclusive = section.getInt("max_y_inclusive", Integer.MIN_VALUE);
        if (maxYInclusive == Integer.MIN_VALUE) {
            maxYInclusive = null;
        }
        Integer minYInclusive = section.getInt("min_y_inclusive", Integer.MIN_VALUE);
        if (minYInclusive == Integer.MIN_VALUE) {
            minYInclusive = null;
        }
        List<?> rawVariables = section.getList("variables");
        if (rawVariables == null) {
            rawVariables = Collections.emptyList();
        }
        List<ConfigVariableCondition> variables =
                rawVariables
                        .stream()
                        .map(ConfigUtil::toConfigurationSection)
                        .map(ConfigVariableCondition::load)
                        .collect(Collectors.toList());
        return new ConfigCondition(inventory, worlds, worldsExcept, maxYInclusive, minYInclusive, variables);
    }

    @Contract(pure = true)
    public @NotNull List<@NotNull ConfigSlotItem> getInventory() {
        return inventory;
    }

    @Contract(pure = true)
    public @NotNull Set<@NotNull String> getWorlds() {
        return worlds;
    }

    @Contract(pure = true)
    public @NotNull Set<@NotNull String> getWorldsExcept() {
        return worldsExcept;
    }

    @Contract(pure = true)
    public @Nullable Integer getMaxYInclusive() {
        return maxYInclusive;
    }

    @Contract(pure = true)
    public @Nullable Integer getMinYInclusive() {
        return minYInclusive;
    }

    @Contract(pure = true)
    public @NotNull List<@NotNull ConfigVariableCondition> getVariables() {
        return variables;
    }

    @Override
    public boolean check(@NotNull Player player, @NotNull Block block) {
        if (!getWorlds().isEmpty() && !getWorlds().contains(block.getWorld().getName())) {
            return false;
        }
        if (getWorldsExcept().contains(block.getWorld().getName())) {
            return false;
        }
        if (getMaxYInclusive() != null && block.getY() > getMaxYInclusive()) {
            return false;
        }
        if (getMinYInclusive() != null && block.getY() < getMinYInclusive()) {
            return false;
        }
        for (ConfigVariableCondition variable : getVariables()) {
            if (!variable.check(player, block)) {
                return false;
            }
        }
        for (ConfigSlotItem item : getInventory()) {
            if (!item.check(player)) {
                return false;
            }
        }
        return true;
    }
}
