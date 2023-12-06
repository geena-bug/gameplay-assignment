package game;

import java.util.Random;

import static game.GamePlay.getShipName;

class PatrolBoat extends Ship {
  private String sName;

  public PatrolBoat(String sName, ShipType preferredTarget) {
    super(preferredTarget);
    this.sName = sName;
    this.nAttack = new Random().nextInt(11) + 10;
    this.nHitPoints = 50;
    this.nArmour = 5;
    this.nRepairRate = 5;
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
            case BATTLESHIP -> 0.0;
            case CARRIER -> 0.1;
            case DESTROYER -> 0.05;
            case SUBMARINE -> 0.05;
            case PATROLBOAT -> 0.8;
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
