package net.azisaba.breakdrop.config;

import net.azisaba.breakdrop.util.ConfigUtil;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

public final class ConfigConditionList implements Condition {
    private final Op op;
    private final List<Condition> conditions;

    @Contract(pure = true)
    public ConfigConditionList(@NotNull Op op, @NotNull List<@NotNull Condition> conditions) {
        this.op = op;
        this.conditions = conditions;
    }

    @Contract(value = "_ -> new", pure = true)
    public static @NotNull ConfigConditionList load(@NotNull ConfigurationSection section) {
        Op op = Op.valueOf(Objects.requireNonNull(section.getString("op")).toUpperCase(Locale.ROOT));
        List<Condition> conditions =
                Objects.requireNonNull(section.getList("conditions"))
                        .stream()
                        .map(ConfigUtil::toConfigurationSection)
                        .map(subSection -> {
                            if (subSection.contains("op")) {
                                return load(subSection);
                            } else {
                                return ConfigCondition.load(subSection);
                            }
                        })
                        .collect(Collectors.toList());
        return new ConfigConditionList(op, conditions);
    }

    @Contract(pure = true)
    public @NotNull Op getOp() {
        return op;
    }

    @Contract(pure = true)
    public @NotNull List<@NotNull Condition> getConditions() {
        return conditions;
    }

    @Contract(pure = true)
    @Override
    public boolean check(@NotNull Player player, @NotNull Block block) {
        switch (getOp()) {
            case AND:
                return getConditions().stream().allMatch(c -> c.check(player, block));
            case OR:
                return getConditions().stream().anyMatch(c -> c.check(player, block));
            default:
                throw new AssertionError();
        }
    }

    public enum Op {
        OR,
        AND,
    }
}
