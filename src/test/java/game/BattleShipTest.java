package game;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class BattleShipTest {

    @Test
    void testGetName() {
        String ShipName="ship a";
        String expectedShipName="ship a";

        BattleShip ship= new BattleShip(ShipName,ShipType.BATTLESHIP);
        String resultShipName=ship.getName();

        assertEquals(expectedShipName,resultShipName);
    }

}
