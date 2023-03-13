package org.localmc.tools.ftbqkeys;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.localmc.tools.ftbqkeys.command.FTBQKeysCommand;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Locale;
import java.util.TreeMap;

@Mod(modid = FTBQKeysModLegacy.MODID, name = FTBQKeysModLegacy.NAME, version = FTBQKeysModLegacy.VERSION)
public class FTBQKeysModLegacy {
    public static Logger LOGGER = LogManager.getLogger();
    public static final String MODID = "ftbqlocalkeys";
    public static final String NAME = "FTBQLocalizationKeys";
    public static final String VERSION = "1.0";

    public static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public static Path gameDir = Minecraft.getMinecraft().gameDir.toPath();
    public static Path configDir = Loader.instance().getConfigDir().toPath();

    public static void saveLang(TreeMap<String, String> transKeys, String lang, File parent) throws IOException {
        File fe = new File(parent, lang.toLowerCase(Locale.ROOT) + ".lang");
        FileUtils.write(fe, FTBQKeysModLegacy.gson.toJson(transKeys), StandardCharsets.UTF_8);
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        LOGGER = event.getModLog();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        ClientCommandHandler.instance.registerCommand(new FTBQKeysCommand());
    }

    @Mod.EventHandler
    public void onSeverStart(FMLServerStartingEvent event) {

    }

}
