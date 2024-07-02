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

//    public static final ForgeConfigSpec.ConfigValue<String> DownloadDestination = BUILDER.comment("The path used to temporarily store the downloaded files")
//        .define("download_destination", ".update");

    public static final ForgeConfigSpec.ConfigValue<String> LatestVersion = BUILDER.comment("(DO NOT CHANGE) The latest downloaded version")
        .define("latest_version", "none");


    public static final ForgeConfigSpec SPEC = BUILDER.build();
}
