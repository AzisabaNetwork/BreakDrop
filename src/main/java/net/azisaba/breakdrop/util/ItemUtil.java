package net.azisaba.breakdrop.util;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.component.CustomData;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ItemUtil {
    @Contract(pure = true)
    public static @NotNull CompoundTag getTag(@Nullable ItemStack item) {
        if (item == null) {
            return new CompoundTag();
        }
        CustomData customData = CraftItemStack.asNMSCopy(item).get(DataComponents.CUSTOM_DATA);
        if (customData == null) {
            return new CompoundTag();
        }
        return customData.copyTag();
    }

    @Contract(pure = true)
    public static @NotNull String getMythicType(@Nullable ItemStack item) {
        if (item == null) {
            return "";
        }
        CompoundTag tag = getTag(item);
        return tag.getCompound("PublicBukkitValues").getString("mythicmobs:type");
    }
}
