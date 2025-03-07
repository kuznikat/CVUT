package cz.cvut.fel.pjv.object;

import cz.cvut.fel.pjv.GamePanel;
import cz.cvut.fel.pjv.entity.Entity;


public class Laser extends Entity {
    GamePanel gp;
    public Laser(GamePanel gp) {
        super(gp);
        type = type_laser;
        name = "Laser";
        face = setup("/objects/laser1", gp.tileSize, gp.tileSize);
        attackValue = 1;
        attackArea.width = 30;
        attackArea.height = 30;
        description = "[" + name + "]\nMagic Laser";


    }
}
