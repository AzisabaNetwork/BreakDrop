package net.azisaba.breakdrop.config;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

public final class ConfigSlotItem {
    private static final EquipmentSlot[] VALID_SLOTS = new EquipmentSlot[] {
        EquipmentSlot.HAND, EquipmentSlot.OFF_HAND, EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET
    };
    private final ConfigItem item;
    private final EquipmentSlot slot;

    @Contract(pure = true)
    public ConfigSlotItem(@NotNull ConfigItem item, @Nullable EquipmentSlot slot) {
        this.item = item;
        this.slot = slot;
    }

    @Contract(value = "_ -> new", pure = true)
    public static @NotNull ConfigSlotItem load(@NotNull ConfigurationSection section) {
        String slotString = section.getString("slot");
        EquipmentSlot slot = slotString == null ? null : resolveEquipmentSlot(slotString);
        if (section.contains("mythic_type")) {
            return new ConfigSlotItem(ConfigMythicItem.load(section), slot);
        } else if (section.contains("crackshot_type")) {
            return new ConfigSlotItem(ConfigCrackShotItem.load(section), slot);
        } else {
            return new ConfigSlotItem(ConfigMinecraftItem.load(section), slot);
        }
    }

    @Contract(pure = true)
    public @NotNull ConfigItem getItem() {
        return item;
    }

    @Contract(pure = true)
    public @Nullable EquipmentSlot getSlot() {
        return slot;
    }

    public boolean check(@NotNull Player player) {
        if (getSlot() == null) {
            for (EquipmentSlot slot : VALID_SLOTS) {
                if (item.check(player.getInventory().getItem(slot))) {
                    return true;
                }
            }
        } else {
            return item.check(player.getInventory().getItem(getSlot()));
        }
        return false;
    }

    private static @NotNull EquipmentSlot resolveEquipmentSlot(@NotNull String slot) {
        switch (slot.toLowerCase(Locale.ROOT)) {
            case "mainhand":
                return EquipmentSlot.HAND;
            case "offhand":
                return EquipmentSlot.OFF_HAND;
            case "helmet":
                return EquipmentSlot.HEAD;
            case "chestplate":
                return EquipmentSlot.CHEST;
            case "leggings":
                return EquipmentSlot.LEGS;
            case "boots":
                return EquipmentSlot.FEET;
            default:
                return EquipmentSlot.valueOf(slot.toUpperCase(Locale.ROOT));
        }
    }
}
