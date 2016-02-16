package com.mygdx.game;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores all of the shops accessible in the game.
 */
public class ShopManager {

    /**
     * List of all shops accessible in the game.
     */
    private List<Shop> shops = new ArrayList<Shop>();

    /**
     * Empty constructor required for JSON deserialisation to work.
     */
    public ShopManager() {
    }

    /**
     * Returns the shop with the specified ID.
     *
     * @param shopID the ID of the shop
     * @return the shop
     */
    public Shop getShop(int shopID) {
        return shops.get(shopID);
    }

    /**
     * @return a string representation of this ShopManager
     */
    @Override
    public String toString() {
        return "ShopManager{" +
                "shops=" + shops +
                '}';
    }


}
