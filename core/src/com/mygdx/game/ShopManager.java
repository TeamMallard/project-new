package com.mygdx.game;
import java.util.ArrayList;
import java.util.List;

/**
 * Stores and manages every skill.
 */

public class ShopManager {

    private List<Shop> shops = new ArrayList<Shop>();


    public ShopManager() {

    }

    /**
     * Adds a new shop.
     * @param shop the Shop to add
     */
    public void addShop(Shop shop) {
    	shop.updateID(shops.size()); //Gives the shop the ID of it's index
        shops.add(shop);//Check size function
    }

    /**
     * Returns the Skill stored at the given index.
     * @param shopID the index of the Sgop to retrieve
     * @return Shop the shop
     */
    public Shop getShop(int shopID) {
        return shops.get(shopID);
    }


    @Override
    public String toString() {
        return "ShopManager{" +
                "shops=" + shops +
                '}';
    }


}
