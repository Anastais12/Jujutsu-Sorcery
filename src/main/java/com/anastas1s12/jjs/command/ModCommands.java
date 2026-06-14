package com.anastas1s12.jjs.command;

import com.anastas1s12.jjs.JujutsuSorcery;
import com.anastas1s12.jjs.sorcerermode.SorcererModeManager;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

/**
 * Command handler for sorcerer mode debug commands
 */
public class ModCommands {

    public static void initialize() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
                registerCommands(dispatcher)
        );
    }

    private static void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("jjs")
                .then(Commands.literal("sorcerer")
                        .then(Commands.literal("toggle")
                                .executes(context -> {
                                    ServerPlayer player = context.getSource().getPlayerOrException();
                                    SorcererModeManager.toggleSorcererMode(player);
                                    return 1;
                                })
                        )
                        .then(Commands.literal("hasEnteredSorcererModeBefore")
                                .then(Commands.argument("value", StringArgumentType.word())
                                        .suggests((context, builder) -> {
                                            builder.suggest("true");
                                            builder.suggest("false");
                                            return builder.buildFuture();
                                        })
                                        .executes(context -> {
                                            ServerPlayer player = context.getSource().getPlayerOrException();
                                            String value = StringArgumentType.getString(context, "value");
                                            boolean hasEntered = Boolean.parseBoolean(value);

                                            int previousState = SorcererModeManager.getSorcererModeData(player).hasEnteredSorcererModeBefore() ? 1 : 0;
                                            SorcererModeManager.getSorcererModeData(player).setHasEnteredSorcererModeBefore(hasEntered);
                                            SorcererModeManager.setSorcererModeData(player, SorcererModeManager.getSorcererModeData(player));
                                            SorcererModeManager.syncPlayerSorcererMode(player);

                                            context.getSource().sendSuccess(
                                                    () -> Component.literal("§6[Sorcerer]§r Reset hasEnteredSorcererModeBefore from " + (previousState == 1 ? "true" : "false") + " to " + hasEntered),
                                                    true
                                            );
                                            return 1;
                                        })
                                )
                        )
                        .then(Commands.literal("technique")
                                .then(Commands.literal("assign")
                                        .then(Commands.argument("id", StringArgumentType.word())
                                                .executes(context -> {
                                                    ServerPlayer player = context.getSource().getPlayerOrException();
                                                    String id = StringArgumentType.getString(context, "id");
                                                    ResourceLocation rl = ResourceLocation.tryParse(id);
                                                    if (rl == null) rl = ResourceLocation.fromNamespaceAndPath(JujutsuSorcery.MOD_ID, id);
                                                    boolean ok = com.anastas1s12.jjs.abilities.AbilityManager.assignTechnique(player, rl);
                                                    context.getSource().sendSuccess(() -> net.minecraft.network.chat.Component.literal(ok ? "Assigned technique " + id : "Technique not found: " + id), true);
                                                    return ok ? 1 : 0;
                                                })
                                        )
                                )
                        )
                )
        );
    }
}

