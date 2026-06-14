package com.anastas1s12.jjs;

import com.anastas1s12.jjs.cursed_energy.CursedEnergyManager;
import com.anastas1s12.jjs.abilities.AbilityManager;
import com.anastas1s12.jjs.abilities.AbilityRegistry;
import com.anastas1s12.jjs.abilities.TechniqueRegistry;
import com.anastas1s12.jjs.networking.payload.PayloadRegistry;
import com.anastas1s12.jjs.sorcerermode.SorcererModeManager;
import com.anastas1s12.jjs.command.ModCommands;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JujutsuSorcery implements ModInitializer {
	public static final String MOD_ID = "jjs";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Initializing Jujutsu Sorcery!");

		CursedEnergyManager.initialize();

		AbilityRegistry.initialize();
		AbilityManager.initialize();

		TechniqueRegistry.initialize();

		SorcererModeManager.initialize();

		ModCommands.initialize();

		PayloadRegistry.registerS2CPayloads();

		LOGGER.info("Initialized Jujutsu Sorcery!");
	}
}