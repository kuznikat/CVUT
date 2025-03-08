package cz.cvut.fel.pjv.object;

import cz.cvut.fel.pjv.GamePanel;
import cz.cvut.fel.pjv.entity.Entity;

public class Plant5 extends Entity {
    GamePanel gp;
    public Plant5(GamePanel gp) {
        super(gp);
        name = "Plant5";
        face = setup("/objects/plant5", gp.tileSize,gp.tileSize);
        description = "[" + name + "]\nPlant5";
    }


}

