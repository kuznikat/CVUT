import cz.cvut.fel.pjv.CollisionChecker;
import cz.cvut.fel.pjv.GamePanel;
import cz.cvut.fel.pjv.KeyHandler;
import cz.cvut.fel.pjv.entity.Entity;
import cz.cvut.fel.pjv.entity.NPC_Yuzo;
import cz.cvut.fel.pjv.entity.Player;
import cz.cvut.fel.pjv.gameData.SaveGame;
import cz.cvut.fel.pjv.object.Laser;
import cz.cvut.fel.pjv.object.Plant1;
import org.junit.jupiter.api.*;

import javax.swing.*;
import java.awt.event.KeyEvent;

import static org.junit.jupiter.api.Assertions.*;

public class GameIntegrationTest {

    GamePanel gp;
    KeyHandler keyHandler;
    SaveGame saveGame;

    @BeforeEach
    public void setup() {
        gp = new GamePanel();
        keyHandler = new KeyHandler(gp);
        saveGame = new SaveGame(gp);
    }

    @Test
    public void testSaveAndLoadGame() {
        gp.player.worldX = 100;
        gp.player.worldY = 200;
        gp.player.inventory.add(new Laser(gp));
        saveGame.save();

        gp.player.worldX = 0;
        gp.player.worldY = 0;
        gp.player.inventory.clear();

        saveGame.load();

        assertEquals(100, gp.player.worldX);
        assertEquals(200, gp.player.worldY);
        assertEquals("Laser", gp.player.inventory.get(0).name);
    }

    @Test
    public void testGameStateTransitions() {
        keyHandler.keyPressed(new KeyEvent(new JTextField(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, ' '));
        assertEquals(gp.playState, gp.gameState);

        keyHandler.keyPressed(new KeyEvent(new JTextField(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ESCAPE, ' '));
        assertEquals(gp.pauseState, gp.gameState);

        keyHandler.keyPressed(new KeyEvent(new JTextField(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ESCAPE, ' '));
        assertEquals(gp.playState, gp.gameState);

        gp.gameState = gp.dialogueState;
        keyHandler.keyPressed(new KeyEvent(new JTextField(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, ' '));
        assertEquals(gp.playState, gp.gameState);
    }


    @Test
    public void testObjectInteraction() {

        // Create a game panel and player
        GamePanel gp = new GamePanel();
        gp.player = new Player(gp, keyHandler);
        gp.player.worldX = 50;
        gp.player.worldY = 50;

        // create and place the Plant1 object in the player's position
        Plant1 plant = new Plant1(gp);
        plant.worldX = gp.player.worldX;
        plant.worldY = gp.player.worldY;
        gp.obj = new Entity[10];
        gp.obj[0] = plant;


        CollisionChecker collisionChecker = new CollisionChecker(gp);

        // checking collision with object
        int objIndex = collisionChecker.checkObject(gp.player, true);

        // check if object was added to inventory
        if (objIndex != 999) {
            gp.player.inventory.add(gp.obj[objIndex]);
        }

        assertTrue(gp.player.inventory.stream().anyMatch(item -> item instanceof Plant1));
    }


    @Test
    public void testPlayerInteractsWithNPC() {
        // Create a game panel and player
        GamePanel gp = new GamePanel();
        gp.player = new Player(gp, keyHandler);
        gp.player.worldX = 50;
        gp.player.worldY = 50;

        // Creating and placing NPCs in the player position
        NPC_Yuzo npc = new NPC_Yuzo(gp);
        npc.worldX = gp.player.worldX;
        npc.worldY = gp.player.worldY;
        gp.npc = new NPC_Yuzo[10];
        gp.npc[0] = npc;

        CollisionChecker collisionChecker = new CollisionChecker(gp);

        //checking collision with NPC
        int npcIndex = collisionChecker.checkEntity(gp.player, gp.npc);

        assertEquals(0, npcIndex);
    }
}

