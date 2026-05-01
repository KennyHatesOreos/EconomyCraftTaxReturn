package com.reazip.economycraft.util;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;

import java.util.function.Predicate;

public final class PermissionCompat {

    private PermissionCompat() {}

    public static Predicate<CommandSourceStack> gamemaster() {
        return source -> {
            // Console, command blocks, rcon
            ServerPlayer player;
            try {
                player = source.getPlayerOrException();
            } catch (Exception e) {
                return true;
            }

            return source.getServer()
                    .getPlayerList()
                    .isOp(player.getGameProfile());
        };
    }

    public static CommandSourceStack withOwnerPermission(CommandSourceStack source) {
        return source;
    }
}
