import cz.cvut.fel.pjv.GamePanel;
import cz.cvut.fel.pjv.KeyHandler;
import cz.cvut.fel.pjv.UI;
import cz.cvut.fel.pjv.entity.Entity;
import cz.cvut.fel.pjv.entity.Player;
import cz.cvut.fel.pjv.object.Laser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PlayerTest {

    private Player player;
    private GamePanel gp;
    private KeyHandler keyHandler;
    private UI ui;

    @BeforeEach
    public void setUp() throws NoSuchFieldException, IllegalAccessException {
        gp = mock(GamePanel.class);
        ui = mock(UI.class);
        keyHandler = new KeyHandler(gp);

        Field uiField = GamePanel.class.getDeclaredField("ui");
        uiField.setAccessible(true);
        uiField.set(gp, ui);

        player = new Player(gp, keyHandler);
        gp.obj = new Entity[10];
    }

    @Test
    public void testRestoreLife() {
        player.life = 3;
        player.invincible = true;
        player.restoreLife();
        assertEquals(player.maxLife, player.life);
        assertFalse(player.invincible);
    }

    @Test
    public void testPickUpObject_InventoryNotFull() {
        player.inventory = new ArrayList<>();
        gp.obj = new Entity[1];
        gp.obj[0] = new Laser(gp);
        player.pickUpObject(0);
        assertEquals(1, player.inventory.size());
        assertNull(gp.obj[0]);
    }

    @Test
    public void testPickUpObject_InventoryFull() {
        player.inventory = new ArrayList<>();
        for (int i = 0; i < player.maxInventorySize; i++) {
            player.inventory.add(new Laser(gp));
        }
        gp.obj = new Entity[1];
        gp.obj[0] = new Laser(gp);
        player.pickUpObject(0);
        assertEquals(player.maxInventorySize, player.inventory.size());
        assertNotNull(gp.obj[0]);
    }

    @Test
    public void testDamageMonster() {
        gp.monster = new Entity[1];
        gp.monster[0] = new Entity(gp);
        gp.monster[0].life = 5;
        gp.monster[0].invincible = false;
        player.attack = 5;
        player.damageMonster(0);
        assertEquals(0, gp.monster[0].life);
        assertTrue(gp.monster[0].invincible);
    }

    @Test
    public void testSelectItem() {
        player.inventory = new ArrayList<>();
        Laser laser = mock(Laser.class);
        laser.type = Entity.type_consumable;
        player.inventory.add(laser);
        player.maxInventorySize = 1;

        // Simulate UI returning the first item
        when(ui.getItemIndexOnSlot()).thenReturn(0);

        player.selectItem();

        Mockito.verify(laser).use(player);
        assertTrue(player.inventory.isEmpty());
    }
}

