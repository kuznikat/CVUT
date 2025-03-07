package cz.cvut.fel.pjv.gameData;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

public class GameData implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public int worldX;
    public int worldY;
    public int maxLife;
    public int life;
    public int level;
    public int strength;
    public int dexterity;
    public int attack;
    public int defence;
    int plants;

    /**
     * For saving players inventory
     *This array is not entity but string, so it can be saved
     */
    ArrayList<String> itemNames = new ArrayList<>();

    //OBJECTS ON MAP
    String mapObjectNames[];
    int mapObjectWorldX[];
    int mapObjectWorldY[];
}
