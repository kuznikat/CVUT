package cz.cvut.fel.pjv.object;

import cz.cvut.fel.pjv.GamePanel;
import cz.cvut.fel.pjv.entity.Entity;

public class Plant3 extends Entity {
    GamePanel gp;
    public Plant3(GamePanel gp) {
        super(gp);
        name = "Plant3";
        face = setup("/objects/plant3",gp.tileSize,gp.tileSize);
        description = "[" + name + "]\nPlant3";
    }

}
