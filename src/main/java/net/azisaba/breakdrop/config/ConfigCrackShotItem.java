package net.azisaba.breakdrop.config;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public final class ConfigCrackShotItem implements ConfigItem {
    private final String crackShotType;

    @Contract(pure = true)
    public ConfigCrackShotItem(@NotNull String crackShotType) {
        this.crackShotType = crackShotType;
    }

    @Contract(value = "_ -> new", pure = true)
    public static @NotNull ConfigCrackShotItem load(@NotNull ConfigurationSection section) {
        String crackShotType = Objects.requireNonNull(section.getString("crackshot_type"), "crackshot_type");
        return new ConfigCrackShotItem(crackShotType);
    }

    @Contract(pure = true)
    public @NotNull String getCrackShotType() {
        return crackShotType;
    }

    @Override
    public boolean check(@Nullable ItemStack item) {
        return false; // CrackShot doesn't have id tag in nbt
    }
}
