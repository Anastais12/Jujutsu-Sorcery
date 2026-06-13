package com.anastas1s12.jjs.abilities.command;

import com.anastas1s12.jjs.JujutsuSorcery;
import com.anastas1s12.jjs.abilities.AbilityManager;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.resources.ResourceLocation;

/**
 * Registers technique-related commands
 */
public class TechniqueCommandHandler {
    public static void initialize() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> register(dispatcher));
    }

    private static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("jjs")
                        .then(Commands.literal("technique")
                                .then(Commands.literal("assign")
                                        .then(Commands.argument("id", StringArgumentType.word())
                                                .executes(context -> {
                                                    ServerPlayer player = context.getSource().getPlayerOrException();
                                                    String id = StringArgumentType.getString(context, "id");
                                                    ResourceLocation rl = ResourceLocation.tryParse(id);
                                                    if (rl == null) rl = ResourceLocation.fromNamespaceAndPath(JujutsuSorcery.MOD_ID, id);
                                                    boolean ok = AbilityManager.assignTechnique(player, rl);
                                                    context.getSource().sendSuccess(() -> Component.literal(ok ? "Assigned technique " + id : "Technique not found: " + id), true);
                                                    return ok ? 1 : 0;
                                                })
                                        )
                                )
                        )
        );
    }
}

