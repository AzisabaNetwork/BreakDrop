package net.azisaba.breakdrop.hook;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.Flags;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * WorldGuard連携。WorldGuardが有効な場合のみ呼び出すこと(クラスロード時にWorldGuardのクラスを参照するため)。
 */
public final class WorldGuardHook {
    private WorldGuardHook() {
    }

    /**
     * プレイヤーがそのブロックを破壊できる(保護されていない、またはバイパス権限を持つ)か。
     */
    public static boolean canBreak(@NotNull Player player, @NotNull Block block) {
        LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);
        var platform = WorldGuard.getInstance().getPlatform();
        if (platform.getSessionManager().hasBypass(localPlayer, BukkitAdapter.adapt(block.getWorld()))) {
            return true;
        }
        return platform.getRegionContainer()
                .createQuery()
                .testBuild(BukkitAdapter.adapt(block.getLocation()), localPlayer, Flags.BLOCK_BREAK);
    }
}
