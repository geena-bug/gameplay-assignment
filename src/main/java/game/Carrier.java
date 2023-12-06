package game;

import static game.GamePlay.getShipName;

import java.util.Random;

public class Carrier extends Ship {

  private final String sName;

  public Carrier(String sName, ShipType preferredTarget) {
    super(preferredTarget);
    this.sName = sName;
    int planes = new Random().nextInt(16) + 10;
    this.nAttack = planes * 10;
    this.nHitPoints = 500;
    this.nArmour = 50;
    this.nRepairRate = 25;
  }

  public String getName() {
    return sName;
  }

  @Override
  public String toString() {
    return "Name = '" + getName() + super.toString();
  }

  @Override
  public void defendAttack(Ship obAttacker) {
    if (!isDestroyed()) {
      double attackSuccessProbability = Math.random();

      ShipType target = sPreferredTarget[new Random().nextInt(sPreferredTarget.length)];

      double targetProbability =
          switch (target) {
            case BATTLESHIP -> 0.1;
            case CARRIER -> 0.05;
            case DESTROYER -> 0.05;
            case SUBMARINE -> 0.4;
            case PATROLBOAT -> 0.4;
          };

      if (attackSuccessProbability > targetProbability) {
        int damage = Math.max(0, obAttacker.getAttack() - nArmour);
        nHitPoints -= damage;
        if (this.nHitPoints < 0) {
          nHitPoints = 0;
        }
        if (isDestroyed()) {
          String obAttackerShipName = getShipName(obAttacker);
          setDestroyedBy(obAttackerShipName);
        }
      }
    }
  }
}
