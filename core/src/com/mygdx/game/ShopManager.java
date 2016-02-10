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
     * Add new Skill.
     * @param skill The Skill to add
     */
    public void addSkill(Shop shop) {

    	shop.updateID(shops.size()); //Gives the shop the ID of it's index
        shops.add(shop);//Check size function
    }

    /**
     * Returns the Skill stored at the given index.
     * @param skillID The index of the Skill to retrieve
     * @return Skill
     */
    public Shop getSkill(int shopID) {
        return shops.get(shopID);
    }


    @Override
    public String toString() {
        return "ShopManager{" +
                "shops=" + shops +
                '}';
    }


}
