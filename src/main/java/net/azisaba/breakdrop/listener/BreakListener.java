package net.azisaba.breakdrop.listener;

import com.gmail.nossr50.mcMMO;
import net.azisaba.breakdrop.BreakDrop;
import net.azisaba.breakdrop.config.ConfigDrop;
import net.azisaba.breakdrop.config.ConfigDropFunction;
import net.azisaba.breakdrop.util.ItemUtil;
import net.minecraft.server.v1_15_R1.NBTTagCompound;
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
        NBTTagCompound mainTag = ItemUtil.getTag(e.getPlayer().getInventory().getItemInMainHand());
        NBTTagCompound offTag = ItemUtil.getTag(e.getPlayer().getInventory().getItemInOffHand());
        if (EXCLUDED_LIFE_ITEM_ID.contains(mainTag.getString("LifeItemId")) || EXCLUDED_LIFE_ITEM_ID.contains(offTag.getString("LifeItemId"))) {
            return;
        }
        if (mcMMO.getPlaceStore().isTrue(e.getBlock().getState())) {
            return;
        }
        for (ConfigDrop drop : plugin.getPluginConfig().getDrops()) {
            if (!drop.canExecute(e.getPlayer(), e.getBlock())) {
                continue;
            }
            for (ConfigDropFunction function : drop.getFunctions()) {
                if (!function.canExecute(e.getPlayer(), e.getBlock())) {
                    continue;
                }
                for (int i = 0; i < function.getCount(); i++) {
                    if (!function.rollChance()) {
                        continue;
                    }
                    function.execute(e.getPlayer(), e.getBlock());
                }
            }
        }
    }
}
