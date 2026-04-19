package net.nanaky.horseshoes.config;

import com.google.gson.*;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.nio.file.*;

public class ModConfigs {

    private static final Path CONFIG_PATH = FabricLoader.getInstance()
            .getConfigDir().resolve("horseshoes.json");

    public static boolean HORSESHOES_DURABILITY;
    public static int HORSESHOES_DURABILITY_THRESHOLD;
    public static boolean VANILLA_ARMOR_DURABILITY;

    public static boolean SLOT_LOCK;

    public static float COPPER_HORSESHOES_SPEED;
    public static float COPPER_HORSESHOES_JUMP;
    public static float COPPER_HORSESHOES_ARMOR;

    public static float IRON_HORSESHOES_SPEED;
    public static float IRON_HORSESHOES_JUMP;
    public static float IRON_HORSESHOES_ARMOR;

    public static float GOLDEN_HORSESHOES_SPEED;
    public static float GOLDEN_HORSESHOES_JUMP;
    public static float GOLDEN_HORSESHOES_ARMOR;

    public static float DIAMOND_HORSESHOES_SPEED;
    public static float DIAMOND_HORSESHOES_JUMP;
    public static float DIAMOND_HORSESHOES_ARMOR;

    public static float NETHERITE_HORSESHOES_SPEED;
    public static float NETHERITE_HORSESHOES_JUMP;
    public static float NETHERITE_HORSESHOES_ARMOR;

    public static void registerConfigs() {
        JsonObject json;

        if (Files.exists(CONFIG_PATH)) {
            try (Reader r = Files.newBufferedReader(CONFIG_PATH)) {
                json = JsonParser.parseReader(r).getAsJsonObject();
            } catch (Exception e) {
                System.err.println("[Horseshoes] Failed to load config, using defaults.");
                json = defaults();
            }
        } else {
            json = defaults();
            try (Writer w = Files.newBufferedWriter(CONFIG_PATH)) {
                new GsonBuilder().setPrettyPrinting().create().toJson(json, w);
                System.out.println("[Horseshoes] Config generated at " + CONFIG_PATH);
            } catch (Exception e) {
                System.err.println("[Horseshoes] Failed to write config.");
            }
        }

        HORSESHOES_DURABILITY           = getBoolean(json, "horseshoes_durability",           true);
        HORSESHOES_DURABILITY_THRESHOLD = getInt    (json, "horseshoes_durability_threshold",  7);
        VANILLA_ARMOR_DURABILITY = getBoolean(json, "vanilla_armor_durability", true);

        SLOT_LOCK                       = getBoolean(json, "slot_lock",                        true);

        COPPER_HORSESHOES_SPEED  = getFloat(json, "copper_horseshoes_speed",   0.10f);
        COPPER_HORSESHOES_JUMP   = getFloat(json, "copper_horseshoes_jump",    0.15f);
        COPPER_HORSESHOES_ARMOR  = getFloat(json, "copper_horseshoes_armor",   0.00f);

        IRON_HORSESHOES_SPEED    = getFloat(json, "iron_horseshoes_speed",     0.10f);
        IRON_HORSESHOES_JUMP     = getFloat(json, "iron_horseshoes_jump",      0.15f);
        IRON_HORSESHOES_ARMOR    = getFloat(json, "iron_horseshoes_armor",     0.00f);

        GOLDEN_HORSESHOES_SPEED  = getFloat(json, "golden_horseshoes_speed",   0.25f);
        GOLDEN_HORSESHOES_JUMP   = getFloat(json, "golden_horseshoes_jump",    0.35f);
        GOLDEN_HORSESHOES_ARMOR  = getFloat(json, "golden_horseshoes_armor",   0.00f);

        DIAMOND_HORSESHOES_SPEED = getFloat(json, "diamond_horseshoes_speed",  0.14f);
        DIAMOND_HORSESHOES_JUMP  = getFloat(json, "diamond_horseshoes_jump",   0.25f);
        DIAMOND_HORSESHOES_ARMOR = getFloat(json, "diamond_horseshoes_armor",  0.00f);

        NETHERITE_HORSESHOES_SPEED = getFloat(json, "netherite_horseshoes_speed", 0.14f);
        NETHERITE_HORSESHOES_JUMP  = getFloat(json, "netherite_horseshoes_jump",  0.25f);
        NETHERITE_HORSESHOES_ARMOR = getFloat(json, "netherite_horseshoes_armor", 0.00f);
    }

    public static void save() {
        try (Writer w = Files.newBufferedWriter(CONFIG_PATH)) {
            new GsonBuilder().setPrettyPrinting().create().toJson(defaults(), w);
        } catch (Exception e) {
            System.err.println("[Horseshoes] Failed to save config.");
        }
    }

    private static JsonObject defaults() {
        JsonObject o = new JsonObject();
        o.addProperty("horseshoes_durability",           true);
        o.addProperty("horseshoes_durability_threshold", 7);
        o.addProperty("vanilla_armor_durability", true);

        o.addProperty("slot_lock",                       true);

        o.addProperty("copper_horseshoes_speed",   0.10f);
        o.addProperty("copper_horseshoes_jump",    0.15f);
        o.addProperty("copper_horseshoes_armor",   0.00f);

        o.addProperty("iron_horseshoes_speed",     0.10f);
        o.addProperty("iron_horseshoes_jump",      0.15f);
        o.addProperty("iron_horseshoes_armor",     0.00f);

        o.addProperty("golden_horseshoes_speed",   0.25f);
        o.addProperty("golden_horseshoes_jump",    0.35f);
        o.addProperty("golden_horseshoes_armor",   0.00f);

        o.addProperty("diamond_horseshoes_speed",  0.15f);
        o.addProperty("diamond_horseshoes_jump",   0.25f);
        o.addProperty("diamond_horseshoes_armor",  0.00f);

        o.addProperty("netherite_horseshoes_speed", 0.15f);
        o.addProperty("netherite_horseshoes_jump",  0.25f);
        o.addProperty("netherite_horseshoes_armor", 0.00f);
        return o;
    }

    private static boolean getBoolean(JsonObject json, String key, boolean def) {
        try { return json.get(key).getAsBoolean(); } catch (Exception e) { return def; }
    }

    private static int getInt(JsonObject json, String key, int def) {
        try { return json.get(key).getAsInt(); } catch (Exception e) { return def; }
    }

    private static float getFloat(JsonObject json, String key, float def) {
        try { return json.get(key).getAsFloat(); } catch (Exception e) { return def; }
    }
}