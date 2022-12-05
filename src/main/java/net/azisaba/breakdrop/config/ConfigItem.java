package net.azisaba.breakdrop.config;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public interface ConfigItem {
    boolean check(@Nullable ItemStack item);
}
