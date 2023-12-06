package game;

import java.util.Random;

import static game.GamePlay.getShipName;

class Submarine extends Ship {
  private String sName;

  public Submarine(String sName, ShipType preferredTarget) {
    super(preferredTarget);
    this.sName = sName;
    this.nAttack = new Random().nextInt(26) + 50;
    this.nHitPoints = 100;
    this.nArmour = 15;
    this.nRepairRate = 10;
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
            case BATTLESHIP -> 0.05;
            case CARRIER -> 0.1;
            case DESTROYER -> 0.75;
            case SUBMARINE -> 0.05;
            case PATROLBOAT -> 0.05;
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
