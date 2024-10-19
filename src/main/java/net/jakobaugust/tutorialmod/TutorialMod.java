package net.jakobaugust.tutorialmod;

import net.fabricmc.api.ModInitializer;

import net.jakobaugust.tutorialmod.item.ModItemGroups;
import net.jakobaugust.tutorialmod.item.ModItems;
import net.jakobaugust.tutorialmod.aitools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.text.Text;
import net.minecraft.server.command.CommandManager;
import com.mojang.brigadier.arguments.StringArgumentType;
// import net.minecraft.server.command.CommandSource;

public class TutorialMod implements ModInitializer {
	public static final String MOD_ID = "tutorialmod";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModItems.registerModItems();
		ModItemGroups.registerItemGroups();
		LOGGER.info("Hello Fabric world!");
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(CommandManager.literal("aigenerate")
                .then(CommandManager.argument("prompt", StringArgumentType.greedyString()) // Use greedyString
                    .executes(context -> {
                        String prompt = StringArgumentType.getString(context, "prompt");
                        context.getSource().sendFeedback(() -> Text.of("You: " + prompt), true);
                        String message = aitools.aigenerate(prompt); // Call the aitools function
                        context.getSource().sendFeedback(() -> Text.of("AI: " + message), true);
                        return 1;
                    })
                ));
        });

	}
}