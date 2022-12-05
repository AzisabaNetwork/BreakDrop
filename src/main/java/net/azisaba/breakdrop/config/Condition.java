package net.azisaba.breakdrop.config;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public interface Condition {
    @Contract(pure = true)
    static @NotNull Condition alwaysTrue() {
        return (player, block) -> true;
    }

    @Contract(pure = true)
    static @NotNull Condition alwaysFalse() {
        return (player, block) -> false;
    }

    boolean check(@NotNull Player player, @NotNull Block block);
}
