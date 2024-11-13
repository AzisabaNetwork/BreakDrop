package net.azisaba.breakdrop.listener;

import com.gmail.nossr50.mcMMO;
import net.azisaba.breakdrop.BreakDrop;
import net.azisaba.breakdrop.config.ConfigDrop;
import net.azisaba.breakdrop.config.ConfigDropFunction;
import net.azisaba.breakdrop.util.ItemUtil;
import net.minecraft.nbt.CompoundTag;
import org.bukkit.Bukkit;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class BreakListener implements Listener {
    private static final Set<String> EXCLUDED_LIFE_ITEM_ID = new HashSet<>(Arrays.asList("56fabea9-e1f9-4e7f-ae78-83e07e8b8767"));
    private final BreakDrop plugin;

    public BreakListener(@NotNull BreakDrop plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockBreak(@NotNull BlockBreakEvent e) {
        CompoundTag mainTag = ItemUtil.getTag(e.getPlayer().getInventory().getItemInMainHand());
        CompoundTag offTag = ItemUtil.getTag(e.getPlayer().getInventory().getItemInOffHand());
        if (EXCLUDED_LIFE_ITEM_ID.contains(mainTag.getString("LifeItemId")) || EXCLUDED_LIFE_ITEM_ID.contains(offTag.getString("LifeItemId"))) {
            return;
        }
        if (Bukkit.getPluginManager().isPluginEnabled("mcMMO") && checkPlaceStore(e.getBlock().getState())) {
            return;
        }
        for (ConfigDrop drop : plugin.getPluginConfig().getDrops()) {
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                if (!drop.canExecute(e.getPlayer(), e.getBlock())) {
                    return;
                }
                for (ConfigDropFunction function : drop.getFunctions()) {
                    if (!function.canExecute(e.getPlayer(), e.getBlock())) {
                        continue;
                    }
                    for (int i = 0; i < function.getCount(); i++) {
                        if (!function.rollChance()) {
                            continue;
                        }
                        Bukkit.getScheduler().runTask(plugin, () -> function.execute(e.getPlayer(), e.getBlock()));
                    }
                }
            });
        }
    }

    private static boolean checkPlaceStore(BlockState blockState) {
        return mcMMO.getPlaceStore().isTrue(blockState);
    }
}
