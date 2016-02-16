package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;

import java.io.IOException;

/**
 * Responsible for loading JSON files.
 */
public class JsonLoader {

    /**
     * Used to deserialise the JSON files.
     */
    private Json json = new Json();

    /**
     * Returns an ItemManager by deserialising the given JSON file.
     *
     * @param file the name of the JSON file
     * @return an ItemManager generated from the JSON file given
     * @throws IOException if the JSON file could not be loaded
     */
    public ItemManager parseItemManager(String file) throws IOException {
        return json.fromJson(ItemManager.class, Gdx.files.internal(file));
    }

    /**
     * Returns a SkillManager by deserialising the given JSON file.
     *
     * @param file the name of the JSON file
     * @return an SkillManager generated from the JSON file given
     * @throws IOException if the JSON file could not be loaded
     */
    public SkillManager parseSkillManager(String file) throws IOException {
        return json.fromJson(SkillManager.class, Gdx.files.internal(file));
    }

    /**
     * Returns a PartyManager by deserialising the given JSON file.
     *
     * @param file the name of the JSON file
     * @return an PartyManager generated from the JSON file given
     * @throws IOException if the JSON file could not be loaded
     */
    public PartyManager parsePartyManager(String file) throws IOException {
        return json.fromJson(PartyManager.class, Gdx.files.internal(file));
    }

    /**
     * Returns a ShopManager by deserialising the given JSON file.
     *
     * @param file the name of the JSON file
     * @return an ShopManager generated from the JSON file given
     * @throws IOException if the JSON file could not be loaded
     */
    public ShopManager parseShopManager(String file) throws IOException {
        return json.fromJson(ShopManager.class, Gdx.files.internal(file));
    }
}
