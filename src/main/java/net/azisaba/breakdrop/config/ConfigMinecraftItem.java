package net.azisaba.breakdrop.config;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class ConfigMinecraftItem implements ConfigItem {
    private final Material type;
    private final int customModelData;
    private final String displayName;
    private final List<String> lore;
    private final Map<Enchantment, Integer> enchantments;

    @Contract(pure = true)
    public ConfigMinecraftItem(
            @NotNull Material type,
            int customModelData,
            @Nullable String displayName,
            @Nullable List<@NotNull String> lore,
            @NotNull Map<@NotNull Enchantment, @NotNull Integer> enchantments
    ) {
        this.type = type;
        this.customModelData = customModelData;
        this.displayName = displayName;
        this.lore = lore;
        this.enchantments = enchantments;
    }

    @Contract(value = "_ -> new", pure = true)
    public static @NotNull ConfigMinecraftItem load(@NotNull ConfigurationSection section) {
        String type = Objects.requireNonNull(section.getString("type"), "type");
        int customModelData = section.getInt("custom_model_data", 0);
        Material bukkitType = Material.matchMaterial(type);
        if (bukkitType == null) {
            throw new IllegalArgumentException("Invalid type: " + type);
        }
        String displayName = section.getString("display_name");
        if (displayName != null) {
            displayName = ChatColor.translateAlternateColorCodes('&', displayName);
        }
        List<?> rawLore = section.getList("lore");
        List<String> lore;
        if (rawLore == null) {
            lore = null;
        } else {
            lore = new ArrayList<>();
            for (Object o : rawLore) {
                lore.add(ChatColor.translateAlternateColorCodes('&', String.valueOf(o)));
            }
        }
        Map<Enchantment, Integer> enchantments = new HashMap<>();
        Object enchantmentsSection = section.get("enchantments");
        if (enchantmentsSection instanceof Map<?, ?>) {
            ((Map<?, ?>) enchantmentsSection).forEach((key, value) -> {
                Enchantment enchantment = Enchantment.getByKey(NamespacedKey.minecraft(String.valueOf(key)));
                if (enchantment == null) {
                    throw new IllegalArgumentException("Invalid enchantment: " + key);
                }
                if (value instanceof Integer) {
                    enchantments.put(enchantment, (Integer) value);
                } else {
                    enchantments.put(enchantment, Integer.parseInt(value.toString()));
                }
            });
        } else if (enchantmentsSection instanceof List<?>) {
            ((List<?>) enchantmentsSection).forEach(value -> {
                String[] split = String.valueOf(value).split(":");
                if (split.length != 2) {
                    throw new IllegalArgumentException("Invalid enchantment: " + value);
                }
                Enchantment enchantment = Enchantment.getByKey(NamespacedKey.minecraft(split[0]));
                if (enchantment == null) {
                    throw new IllegalArgumentException("Invalid enchantment: " + value);
                }
                enchantments.put(enchantment, Integer.parseInt(split[1]));
            });
        }
        return new ConfigMinecraftItem(bukkitType, customModelData, displayName, lore, enchantments);
    }

    @Contract(pure = true)
    public @NotNull Material getType() {
        return type;
    }

    @Contract(pure = true)
    public int getCustomModelData() {
        return customModelData;
    }

    @Contract(pure = true)
    public @Nullable String getDisplayName() {
        return displayName;
    }

    @Contract(pure = true)
    public @Nullable List<@NotNull String> getLore() {
        return lore;
    }

    @Contract(pure = true)
    public @NotNull Map<@NotNull Enchantment, @NotNull Integer> getEnchantments() {
        return enchantments;
    }

    @Contract("null -> false")
    @Override
    public boolean check(@Nullable ItemStack item) {
        if (item == null || item.getType() != getType()) {
            return false;
        }
        ItemMeta meta = item.hasItemMeta() ? item.getItemMeta() : null;
        if (getCustomModelData() != 0) {
            if (meta == null || !meta.hasCustomModelData()) {
                return false;
            }
            if (meta.getCustomModelData() != getCustomModelData()) {
                return false;
            }
        }
        if (getDisplayName() != null) {
            if (meta == null || !meta.hasDisplayName()) {
                return false;
            }
            if (!meta.getDisplayName().equals(getDisplayName())) {
                return false;
            }
        }
        if (getLore() != null) {
            if (meta == null || !meta.hasLore()) {
                return false;
            }
            List<String> itemLore = meta.getLore();
            if (itemLore == null || itemLore.size() != getLore().size()) {
                return false;
            }
            for (int i = 0; i < itemLore.size(); i++) {
                if (!Objects.equals(itemLore.get(i), getLore().get(i))) {
                    return false;
                }
            }
        }
        if (!getEnchantments().isEmpty()) {
            if (meta == null || !meta.hasEnchants()) {
                return false;
            }
            for (Map.Entry<Enchantment, Integer> entry : getEnchantments().entrySet()) {
                if (!meta.hasEnchant(entry.getKey())) {
                    return false;
                }
                if (meta.getEnchantLevel(entry.getKey()) != entry.getValue()) {
                    return false;
                }
            }
        }
        return true;
    }
}
