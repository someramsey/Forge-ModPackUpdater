package com.ramsey.updater;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Main.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec.ConfigValue<String> FetchUrl = BUILDER.comment("The url to fetch the update info")
        .define("fetch_url", "http://localhost:3000");

    public static final ForgeConfigSpec.ConfigValue<Integer> FetchTimeout = BUILDER.comment("The timeout for the connection when fetching the update info")
        .define("fetch_timeout", 5000);

    public static final ForgeConfigSpec.ConfigValue<String> RootDir = BUILDER.comment("The root directory used for operations")
        .define("root_dir", ".\\updater\\");

    public static final ForgeConfigSpec.ConfigValue<String> PackagePath = BUILDER.comment("The filename used to temporarily store the payload file")
        .define("package_filename", "pack.zip");

    public static final ForgeConfigSpec.ConfigValue<String> ScriptPath = BUILDER.comment("The filename used run the script")
        .define("script_filename", "script.bat");












    public static final ForgeConfigSpec.ConfigValue<String> LatestVersion = BUILDER.comment("(DO NOT CHANGE) The latest downloaded version")
        .define("latest_version", "none");


    public static final ForgeConfigSpec SPEC = BUILDER.build();
}
