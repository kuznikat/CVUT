package cz.cvut.fel.pjv.object;

import cz.cvut.fel.pjv.GamePanel;
import cz.cvut.fel.pjv.entity.Entity;

import java.util.Objects;

public class Plant1 extends Entity {
    GamePanel gp;
    public Plant1(GamePanel gp) {
        super(gp);
        name = "Plant1";
        face = setup("/objects/plant1", gp.tileSize, gp.tileSize);
        description = "[" + name + "]\nPlant1";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Plant1 plant = (Plant1) obj;
        return worldX == plant.worldX && worldY == plant.worldY && // Další relevantní pole
                Objects.equals(name, plant.name); // Například porovnání podle jména
    }

    @Override
    public int hashCode() {
        return Objects.hash(worldX, worldY, name); // Například hash kód podle jména
    }
}
