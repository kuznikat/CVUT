package cz.cvut.fel.pjv.tile;

import cz.cvut.fel.pjv.GamePanel;
import cz.cvut.fel.pjv.Utility;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TileControler {
    GamePanel gp;
    public Tile[] tile;
    public int mapTileNum[][]; //store all the numbers from map


    public TileControler(GamePanel gp) {
        this.gp = gp;
        tile = new Tile[50]; // we are going to create 10 kinds of tiles(water,ground etc)
        mapTileNum = new int[gp.maxWorldCol][gp.maxWorldRow];
        getTileImage();
        loadMap("/maps/worldV2.txt");
    }

    public void getTileImage(){

        //Placeholder
        setup(0,"grass00",false);
        setup(1,"grass00",false);
        setup(2,"grass00",false);
        setup(3,"grass00",false);
        setup(4,"grass00",false);
        setup(5,"grass00",false);
        setup(6,"grass00",false);
        setup(7,"grass00",false);
        setup(8,"grass00",false);
        setup(9,"grass00",false);
        //Placeholder


        setup(10,"grass00",false);
        setup(11,"grass01",false);
        setup(12,"w0",true);
        setup(13,"w1",true);
        setup(14,"w2",true);
        setup(15,"w3",true);
        setup(16,"w4",true);
        setup(17,"w5",true);
        setup(18,"w6",true);
        setup(19,"w7",true);
        setup(20,"w8",true);
        setup(21,"w9",true);
        setup(22,"w10",true);
        setup(23,"w11",true);
        setup(24,"w12",true);
        setup(25,"w13",true);
        setup(26,"road0",false);
        setup(27,"r1",false);
        setup(28,"r2",false);
        setup(29,"r3",false);
        setup(30,"r4",false);
        setup(31,"r5",false);
        setup(32,"r6",false);
        setup(33,"r7",false);
        setup(34,"r8",false);
        setup(35,"r9",false);
        setup(36,"r10",false);
        setup(37,"r11",false);
        setup(38,"r12",false);
        setup(39,"earth",false);
        setup(40,"stone",true);
        setup(41,"tree",true);
        setup(42,"win",true);


    }

    public void setup(int index, String imageName, boolean collision){
        Utility ut = new Utility();

        try{
            tile[index] = new Tile();
            tile[index].image = ImageIO.read(getClass().getResourceAsStream("/tiles/" + imageName + ".png"));
            tile[index].image = ut.scaleImg(tile[index].image, gp.tileSize,gp.tileSize);
            tile[index].collision = collision;

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void loadMap(String mapPath){
        try {
            InputStream is = getClass().getResourceAsStream(mapPath);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int col = 0;
            int row = 0;

            while(col < gp.maxWorldCol && row < gp.maxWorldRow){
                String line = br.readLine();
                while (col < gp.maxWorldCol) {
                    String numbers[] = line.split(" "); // split the string at a space

                    int num = Integer.parseInt(numbers[col]);

                    mapTileNum[col][row] = num;
                    col++;
                }
                if(col == gp.maxWorldCol){
                    col = 0;
                    row++;
                }
            }
            br.close();

        }catch (Exception e) {

        }
    }

    public void draw(Graphics2D g2){

        int worldCol = 0;
        int worldRow = 0;


        while(worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow){

            int tileNum = mapTileNum[worldCol][worldRow];

            int worldX = worldCol * gp.tileSize; // position of a map
            int worldY = worldRow * gp.tileSize;
            int screenX = worldX - gp.player.worldX + gp.player.screenX; //where on map to draw a character
            int screenY = worldY - gp.player.worldY + gp.player.screenY;

            if(worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
                    worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
                    worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
                    worldY - gp.tileSize < gp.player.worldY + gp.player.screenY){

                g2.drawImage(tile[tileNum].image, screenX, screenY, null);
            }
            worldCol++; //increasing

            if(worldCol == gp.maxWorldCol){
                //when reaching the max for columns - resetting and increasing rows
                worldCol = 0;
                worldRow ++;
            }
        }
    }
}
