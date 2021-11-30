package kaptainwutax.seedcracker.command;

import kaptainwutax.seedcracker.SeedCracker;
import kaptainwutax.seedcracker.cracker.DataAddedEvent;
import kaptainwutax.seedcracker.cracker.HashedSeedData;
import kaptainwutax.seedcracker.finder.FinderQueue;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.s2c.play.ChunkDataS2CPacket;
import net.minecraft.network.packet.s2c.play.CommandTreeS2CPacket;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerRespawnS2CPacket;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.math.ChunkPos;

import static net.minecraft.server.command.CommandManager.literal;

public class CrackerCommand extends ClientCommand {

	@Override
	public String getName() {
		return "recrack";
	}
	
        //PRE-1.16 SUPPORTED GENERATOR TYPES
        //GeneratorTypeData generatorTypeData = new GeneratorTypeData(packet.getGeneratorType());

        //Log.warn("Fetched the generator type [" +
        //        I18n.translate(generatorTypeData.getGeneratorType().getStoredName()).toUpperCase() + "].");

        //if(!SeedCracker.get().getDataStorage().addGeneratorTypeData(generatorTypeData)) {
        //    Log.error("THIS GENERATOR IS NOT SUPPORTED!");
        //    Log.error("Overworld biome search WILL NOT run.");
        //}

        HashedSeedData hashedSeedData = new HashedSeedData(packet.getSha256Seed());

        if(SeedCracker.get().getDataStorage().addHashedSeedData(hashedSeedData, DataAddedEvent.POKE_BIOMES)) {
            Log.warn("Fetched hashed world seed [" + hashedSeedData.getHashedSeed() + "].");
        }

        SeedCracker.get().setActive(SeedCracker.get().isActive());
    }

    @Inject(method = "onPlayerRespawn", at = @At(value = "TAIL"))
    public void onPlayerRespawn(PlayerRespawnS2CPacket packet, CallbackInfo ci) {
        HashedSeedData hashedSeedData = new HashedSeedData(packet.getSha256Seed());

        if(SeedCracker.get().getDataStorage().addHashedSeedData(hashedSeedData, DataAddedEvent.POKE_BIOMES)) {
            Log.warn("Fetched hashed world seed [" + hashedSeedData.getHashedSeed() + "].");
        }
    }

}
