package cz.cvut.fel.pjv.object;

import cz.cvut.fel.pjv.GamePanel;
import cz.cvut.fel.pjv.entity.Entity;

public class Heart extends Entity {
//    GamePanel gp;
    public Heart(GamePanel gp) {
//        this.gp = gp;
        super(gp);
        name = "Heart";
        image = setup("/objects/h_full",gp.tileSize,gp.tileSize);
        image2 = setup("/objects/h_half",gp.tileSize,gp.tileSize);
        image3 = setup("/objects/h_blank" ,gp.tileSize,gp.tileSize);

    }
}
