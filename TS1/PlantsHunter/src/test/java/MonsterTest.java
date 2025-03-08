import cz.cvut.fel.pjv.CollisionChecker;
import cz.cvut.fel.pjv.GamePanel;
import cz.cvut.fel.pjv.monster.Monster;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

public class MonsterTest {
    private Monster monster;
    private GamePanel gamePanel;


    @BeforeEach
    public void setUp() {
        gamePanel = Mockito.mock(GamePanel.class);
        monster = new Monster(gamePanel);
    }

    @Test
    public void testConstructorInitializesFieldsCorrectly() {
        assertEquals("Frog", monster.name);
        assertEquals(1, monster.speed);
        assertEquals(4, monster.maxLife);
        assertEquals(monster.maxLife, monster.life);
        assertEquals(3, monster.attack);
        assertEquals(0, monster.defence);
        assertNotNull(monster.faceLeft);
        assertNotNull(monster.faceRight);
        assertNotNull(monster.face);
        assertNotNull(monster.back);
    }

    @Test
    public void testDamageReaction() {
        monster.actionLockCounter = 50;
        gamePanel.player = Mockito.mock(cz.cvut.fel.pjv.entity.Player.class);
        gamePanel.player.direction = "left";
        monster.damageReaction();
        assertEquals(0, monster.actionLockCounter);
        assertEquals("left", monster.direction);
    }
}


