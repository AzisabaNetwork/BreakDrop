package net.azisaba.breakdrop.util;

import net.minecraft.server.v1_15_R1.NBTTagCompound;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ItemUtil {
    @Contract(pure = true)
    public static @NotNull NBTTagCompound getTag(@Nullable ItemStack item) {
        if (item == null) {
            return new NBTTagCompound();
        }
        net.minecraft.server.v1_15_R1.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tag = nmsItem.getTag();
        if (tag == null) {
            return new NBTTagCompound();
        }
        return tag;
    }

    @Contract(pure = true)
    public static @NotNull String getMythicType(@Nullable ItemStack item) {
        if (item == null) {
            return "";
        }
        NBTTagCompound tag = getTag(item);
        return tag.getString("MYTHIC_TYPE");
    }
}
