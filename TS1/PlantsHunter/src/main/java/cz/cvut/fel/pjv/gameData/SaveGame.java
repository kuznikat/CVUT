package cz.cvut.fel.pjv.gameData;

import cz.cvut.fel.pjv.GamePanel;
import cz.cvut.fel.pjv.UI;
import cz.cvut.fel.pjv.entity.Entity;
import cz.cvut.fel.pjv.object.*;

import java.awt.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SaveGame {

    private static final Logger LOGGER = Logger.getLogger(UI.class.getName());
    GamePanel gp;


    public SaveGame(GamePanel gp) {
        this.gp = gp;
    }

    /**
     * @param itemName name of the item that will be later stocked
     * @return obj
     */
    public Entity getOBJ(String itemName){

        Entity obj = null;

        switch (itemName){
            case "Soda" : obj = new Soda(gp);break;
            case "Strawberry" : obj = new Strawberry(gp);break;
            case "Drop" : obj = new Drop(gp);break;
            case "Laser" : obj = new Laser(gp);break;
            case "Plant1" : obj = new Plant1(gp);break;
            case "Plant2" : obj = new Plant2(gp);break;
            case "Plant3" : obj = new Plant3(gp);break;
            case "Plant4" : obj = new Plant4(gp);break;
            case "Plant5" : obj = new Plant5(gp);break;
        }
        return obj;
    }

    /**
     * Saving game data to a file
     */
    public void save(){
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("save.data")))) {
            GameData gd = new GameData();

            //PLAYER STATS
            gd.worldX = gp.player.worldX;
            gd.worldY = gp.player.worldY;
            gd.level = gp.player.level;
            gd.maxLife = gp.player.maxLife;
            gd.life = gp.player.life;
            gd.strength = gp.player.strength;
            gd.dexterity = gp.player.dexterity;
            gd.attack = gp.player.attack;
            gd.defence = gp.player.defence;
            gd.plants = gp.player.plants;


            // INVENTORY
            for (Entity item : gp.player.inventory) {
                gd.itemNames.add(item.name);
            }


            //OBJECTS ON MAP
            gd.mapObjectNames = new String[gp.obj.length];
            gd.mapObjectWorldX = new int[gp.obj.length];
            gd.mapObjectWorldY = new int[gp.obj.length];

             for(int i = 0; i < gp.obj.length; i++){
                 if(gp.obj[i] == null){
                     gd.mapObjectNames[i] = "NONE";
                 }
                 else {
                     gd.mapObjectNames[i] = gp.obj[i].name;
                     gd.mapObjectWorldX[i] = gp.obj[i].worldX;
                     gd.mapObjectWorldY[i] = gp.obj[i].worldY;
                 }
             }
            oos.writeObject(gd);

        } catch (FileNotFoundException e) {
            LOGGER.log(Level.SEVERE, "File not found", e);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to save data", e);
        }
    }

    /**
     * Loading data when game reseated
     */
    public void load(){
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File("save.data")))) {
            GameData gd = (GameData) ois.readObject();

            gp.player.worldX = gd.worldX;
            gp.player.worldY = gd.worldY;
            gp.player.level = gd.level;
            gp.player.maxLife = gd.maxLife;
            gp.player.life = gd.life;
            gp.player.strength = gd.strength;
            gp.player.dexterity = gd.dexterity;
            gp.player.attack = gd.attack;
            gp.player.defence = gd.defence;
            gp.player.plants = gd.plants;


            gp.player.inventory.clear();
            for (String itemName : gd.itemNames) {
                gp.player.inventory.add(getOBJ(itemName));
            }
            gp.gameState = gp.playState;

            // Clear existing objects on the map
            for (int i = 0; i < gp.obj.length; i++) {
                gp.obj[i] = null;
            }

            // Load objects on the map
            for (int i = 0; i < gd.mapObjectNames.length; i++) {
                if (!gd.mapObjectNames[i].equals("NONE")) {
                    gp.obj[i] = getOBJ(gd.mapObjectNames[i]);
                    gp.obj[i].worldX = gd.mapObjectWorldX[i];
                    gp.obj[i].worldY = gd.mapObjectWorldY[i];
                }
            }

        } catch (FileNotFoundException e) {
            LOGGER.log(Level.SEVERE, "File not found", e);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to load data", e);
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Class not found", e);
        }
    }
}
