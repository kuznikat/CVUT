package cz.cvut.fel.pjv;

import cz.cvut.fel.pjv.entity.NPC_Yuzo;
import cz.cvut.fel.pjv.monster.Monster;
import cz.cvut.fel.pjv.object.*;


public class AssetSetter {
    GamePanel gp;

    public AssetSetter(GamePanel gp) {
        this.gp = gp;
    }

    public void setObject(){
         int i = 0;

        gp.obj[i] = new Soda(gp);
        gp.obj[i].worldX = gp.tileSize * 35;
        gp.obj[i].worldY = gp.tileSize * 33;
        i++;

        gp.obj[i] = new Soda(gp);
        gp.obj[i].worldX = gp.tileSize * 14;
        gp.obj[i].worldY = gp.tileSize * 25;
        i++;

        gp.obj[i] = new Plant1(gp);
        gp.obj[i].worldX = 16 * gp.tileSize;
        gp.obj[i].worldY = 14 * gp.tileSize;
        i++;

        gp.obj[i] = new Plant2(gp);
        gp.obj[i].worldX = 10 * gp.tileSize;
        gp.obj[i].worldY = 7 * gp.tileSize;
        i++;

        gp.obj[i] = new Plant3(gp);
        gp.obj[i].worldX = 30 * gp.tileSize;
        gp.obj[i].worldY = 29 * gp.tileSize;
        i++;

        gp.obj[i] = new Plant4(gp);
        gp.obj[i].worldX = 8 * gp.tileSize;
        gp.obj[i].worldY = 41 * gp.tileSize;
        i++;

        gp.obj[i] = new Plant5(gp);
        gp.obj[i].worldX = 41 * gp.tileSize;
        gp.obj[i].worldY = 7 * gp.tileSize;
        i++;

        gp.obj[i] = new Strawberry(gp);
        gp.obj[i].worldX = 26 * gp.tileSize;
        gp.obj[i].worldY = 7 * gp.tileSize;
        i++;

        gp.obj[i] = new Strawberry(gp);
        gp.obj[i].worldX = 30 * gp.tileSize;
        gp.obj[i].worldY = 37 * gp.tileSize;
        i++;
        gp.obj[i] = new Drop(gp);
        gp.obj[i].worldX = 36 * gp.tileSize;
        gp.obj[i].worldY = 40 * gp.tileSize;
        i++;
   }

    public void setNPC(){
        gp.npc[0] = new NPC_Yuzo(gp);
        gp.npc[0].worldX = gp.tileSize * 21;
        gp.npc[0].worldY = gp.tileSize * 21;
    }

    public void setMonster(){

        int i = 0;

        gp.monster[i] = new Monster(gp);
        gp.monster[i].worldX = gp.tileSize * 23;
        gp.monster[i].worldY = gp.tileSize * 40;
        i++;
        gp.monster[i] = new Monster(gp);
        gp.monster[i].worldX = gp.tileSize * 39;
        gp.monster[i].worldY = gp.tileSize * 10;
        i++;
        gp.monster[i] = new Monster(gp);
        gp.monster[i].worldX = gp.tileSize * 24;
        gp.monster[i].worldY = gp.tileSize * 37;
        i++;
        gp.monster[i] = new Monster(gp);
        gp.monster[i].worldX = gp.tileSize * 34;
        gp.monster[i].worldY = gp.tileSize * 42;
        i++;
        gp.monster[i] = new Monster(gp);
        gp.monster[i].worldX = gp.tileSize * 38;
        gp.monster[i].worldY = gp.tileSize * 42;
        i++;
        gp.monster[i] = new Monster(gp);
        gp.monster[i].worldX = gp.tileSize * 11;
        gp.monster[i].worldY = gp.tileSize * 32;
        i++;
        gp.monster[i] = new Monster(gp);
        gp.monster[i].worldX = gp.tileSize * 34;
        gp.monster[i].worldY = gp.tileSize * 29;
        i++;

    }
}
