package net.azisaba.breakdrop.config;

import net.azisaba.breakdrop.util.ConfigUtil;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public final class ConfigDrop {
    private final List<ConfigDropFunction> functions;
    private final Condition condition;

    public ConfigDrop(@NotNull List<@NotNull ConfigDropFunction> functions, @NotNull Condition condition) {
        this.functions = functions;
        this.condition = condition;
    }

    @Contract(pure = true)
    public static @NotNull ConfigDrop load(@NotNull ConfigurationSection section) {
        List<ConfigDropFunction> functions =
                Objects.requireNonNull(section.getList("functions"))
                        .stream()
                        .map(ConfigUtil::toConfigurationSection)
                        .map(ConfigDropFunction::load)
                        .collect(Collectors.toList());
        Condition condition;
        ConfigurationSection conditionSection = ConfigUtil.toConfigurationSection(section.get("condition"));
        if (conditionSection != null) {
            if (conditionSection.contains("op")) {
                condition = ConfigConditionList.load(conditionSection);
            } else {
                condition = ConfigCondition.load(conditionSection);
            }
        } else {
            condition = Condition.alwaysTrue();
        }
        return new ConfigDrop(functions, condition);
    }

    @Contract(pure = true)
    public @NotNull List<@NotNull ConfigDropFunction> getFunctions() {
        return functions;
    }

    @Contract(pure = true)
    public @NotNull Condition getCondition() {
        return condition;
    }

    public boolean canExecute(@NotNull Player player, @NotNull Block block) {
        return getCondition().check(player, block);
    }
}
