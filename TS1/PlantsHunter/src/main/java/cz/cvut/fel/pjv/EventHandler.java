package cz.cvut.fel.pjv;

public class EventHandler {

    GamePanel gp;
    EventRect eventRect[][];

    int previousEventX, previousEventY;
    boolean canTouchEvent = true;

    /**
     * Method for calculating and setting where on map will be event
     */
    public EventHandler(GamePanel gp) {
        this.gp = gp;

        eventRect = new EventRect[gp.maxWorldCol][gp.maxWorldRow];

        int col = 0;
        int row = 0;
        while(col < gp.maxWorldCol && row < gp.maxWorldRow) {

            eventRect[col][row] = new EventRect();
            eventRect[col][row].x = 23;
            eventRect[col][row].y = 23;
            eventRect[col][row].width = 2;
            eventRect[col][row].height = 2;
            eventRect[col][row].eventRectDefaultX = eventRect[col][row].x;
            eventRect[col][row].eventRectDefaultY = eventRect[col][row].y;

            col++;
            if(col == gp.maxWorldCol){
                col = 0;
                row ++;
            }
        }
    }

    /**
     * Check where the player character from the last event
     *
     */
    public void checkEvent(){

        //Check if the player character is more than 1 tile away from the last event
        int xDistance = Math.abs(gp.player.worldX - previousEventX); // these methods return absolute values of calculations
        int yDistance = Math.abs(gp.player.worldY - previousEventY);
        int distance = Math.max(xDistance, yDistance);
        if(distance > gp.tileSize) {
            canTouchEvent = true;
        }

        if(canTouchEvent) {
            if(hit(26, 16, "any")){
                firstTeleport(26,15,gp.dialogueState);}
            if(hit(16, 14, "any")){
                secondTeleport(16,14,gp.dialogueState);}
        }
    }

    /**
     * Method to set the area where event will happen if player hits this area
     * @param reqDirection direction that required for event
     * @return hit
     */
    public boolean hit( int col, int row, String reqDirection) {
        boolean hit = false;

        gp.player.solidArea.x = gp.player.worldX + gp.player.solidArea.x; // getting players current solid area
        gp.player.solidArea.y = gp.player.worldY + gp.player.solidArea.y;
        eventRect[col][row].x = col * gp.tileSize + eventRect[col][row].x;
        eventRect[col][row].y = row * gp.tileSize + eventRect[col][row].y;
        if(gp.player.solidArea.intersects(eventRect[col][row]) && eventRect[col][row].eventDone == false){
            if(gp.player.direction.contentEquals(reqDirection) || reqDirection.contentEquals("any")){
                hit = true;

                previousEventX = gp.player.worldX;
                previousEventY = gp.player.worldY;
            }
        }

        gp.player.solidArea.x = gp.player.solidAreaDefaultX;
        gp.player.solidArea.y = gp.player.solidAreaDefaultY;
        eventRect[col][row].x = eventRect[col][row].eventRectDefaultX;
        eventRect[col][row].y = eventRect[col][row].eventRectDefaultY;

        return hit;
    }

    /**
     * Method for teleport1 event
     */
    public void firstTeleport(int col, int row, int gameState) {
        gp.gameState = gameState;
        gp.ui.currentDialogue = "Teleport!\n Your progress was saved";
        gp.player.worldX = gp.tileSize * 37;
        gp.player.worldY = gp.tileSize * 10;
        eventRect[col][row].eventDone = true;
        canTouchEvent = false;
        gp.saveGame.save();
    }

    /**
     * Method for teleport2 event
     */
    public void secondTeleport(int col, int row, int gameState){
        gp.gameState = gameState;
        gp.ui.currentDialogue = "Teleport!\n Your progress was saved";
        gp.player.worldX = gp.tileSize * 19;
        gp.player.worldY = gp.tileSize * 39;
        eventRect[col][row].eventDone = true;
        canTouchEvent = false;
        gp.saveGame.save();
    }
}
