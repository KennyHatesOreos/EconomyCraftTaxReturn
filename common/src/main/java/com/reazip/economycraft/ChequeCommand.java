package com.reazip.economycraft;

import com.mojang.brigadier.arguments.LongArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.ItemLore;

import java.util.List;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public final class ChequeCommand {
    private static final String CHEQUE_KEY = "economycraft_cheque";
    private static final String AMOUNT_KEY = "economycraft_cheque_amount";

    private ChequeCommand() {}

    public static LiteralArgumentBuilder<CommandSourceStack> register() {
        return literal("cheque")
                .then(literal("redeem")
                        .executes(ChequeCommand::redeemCheque))
                .then(argument("amount", LongArgumentType.longArg(1, EconomyManager.MAX))
                        .executes(ctx -> createCheque(ctx, LongArgumentType.getLong(ctx, "amount"))));
    }

    private static int createCheque(CommandContext<CommandSourceStack> ctx, long amount) {
        CommandSourceStack source = ctx.getSource();
        ServerPlayer player = getPlayer(source);
        if (player == null) return 0;

        EconomyManager manager = EconomyCraft.getManager(source.getServer());
        if (!manager.removeMoney(player.getUUID(), amount)) {
            source.sendFailure(Component.literal("Not enough balance.").withStyle(ChatFormatting.RED));
            return 0;
        }

        ItemStack cheque = createChequeItem(amount);
        if (!player.getInventory().add(cheque)) {
            manager.addMoney(player.getUUID(), amount);
            source.sendFailure(Component.literal("No inventory space for the cheque.").withStyle(ChatFormatting.RED));
            return 0;
        }

        player.sendSystemMessage(Component.literal("Created cheque for " + EconomyCraft.formatMoney(amount) + ".")
                .withStyle(ChatFormatting.GREEN));
        return 1;
    }

    private static int redeemCheque(CommandContext<CommandSourceStack> ctx) {
        CommandSourceStack source = ctx.getSource();
        ServerPlayer player = getPlayer(source);
        if (player == null) return 0;

        ItemStack hand = player.getMainHandItem();
        long amount = getChequeAmount(hand);
        if (amount <= 0L) {
            source.sendFailure(Component.literal("Hold an EconomyCraft cheque in your main hand.").withStyle(ChatFormatting.RED));
            return 0;
        }

        hand.shrink(1);
        if (hand.isEmpty()) {
            player.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
        }

        EconomyCraft.getManager(source.getServer()).addMoney(player.getUUID(), amount);
        player.sendSystemMessage(Component.literal("Redeemed cheque for " + EconomyCraft.formatMoney(amount) + ".")
                .withStyle(ChatFormatting.GREEN));
        return 1;
    }

    private static ItemStack createChequeItem(long amount) {
        ItemStack cheque = new ItemStack(Items.PAPER);
        CompoundTag tag = new CompoundTag();
        tag.putBoolean(CHEQUE_KEY, true);
        tag.putLong(AMOUNT_KEY, amount);
        cheque.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
        cheque.set(DataComponents.CUSTOM_NAME, Component.literal("Cheque")
                .withStyle(s -> s.withItalic(false).withColor(ChatFormatting.GOLD)));
        cheque.set(DataComponents.LORE, new ItemLore(List.of(
                Component.literal("Value: " + EconomyCraft.formatMoney(amount))
                        .withStyle(s -> s.withItalic(false).withColor(ChatFormatting.GREEN)),
                Component.literal("Use /cheque redeem to deposit")
                        .withStyle(s -> s.withItalic(false).withColor(ChatFormatting.GRAY))
        )));
        return cheque;
    }

    private static long getChequeAmount(ItemStack stack) {
        if (stack == null || stack.isEmpty() || !stack.is(Items.PAPER)) return 0L;

        CustomData data = stack.get(DataComponents.CUSTOM_DATA);
        if (data == null || data.isEmpty()) return 0L;

        CompoundTag tag = data.copyTag();
        if (!tag.getBoolean(CHEQUE_KEY)) return 0L;

        long amount = tag.getLong(AMOUNT_KEY);
        return Math.max(0L, Math.min(EconomyManager.MAX, amount));
    }

    private static ServerPlayer getPlayer(CommandSourceStack source) {
        try {
            return source.getPlayerOrException();
        } catch (Exception e) {
            source.sendFailure(Component.literal("Only players can use this command.").withStyle(ChatFormatting.RED));
            return null;
        }
    }
}
