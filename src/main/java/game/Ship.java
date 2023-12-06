package game;


abstract class Ship {
    protected int nAttack;
    protected int nRepairRate;
    protected int nArmour;
    protected int nHitPoints;
    protected ShipType[] sPreferredTarget;
    protected String destroyedBy;


    Ship(ShipType sPreferredTarget, int initialHitPoints, int initialArmor) {
        this.sPreferredTarget = new ShipType[]{sPreferredTarget};
        this.destroyedBy = null;
        this.nHitPoints = initialHitPoints;
        this.nArmour = initialArmor;
    }


    Ship(ShipType sPreferredTarget) {
        this(sPreferredTarget, 0, 0);
    }


    public int getAttack() {
        return nAttack;
    }

    public int getArmour() {
        return nArmour;
    }

    public boolean isDestroyed() {
        return nHitPoints <= 0;
    }

    public void setDestroyedBy(String shipName) {
        destroyedBy = shipName;
    }

    public void repair() {
        if (nHitPoints > 0) {
            nHitPoints += nRepairRate;
        }
        switch (this.getClass().getSimpleName()) {
            case "BattleShip" -> nHitPoints = Math.min(nHitPoints, 300);
            case "Carrier" -> nHitPoints = Math.min(nHitPoints, 500);
            case "PatrolBoat", "Destroyer" -> nHitPoints = Math.min(nHitPoints, 100);
            case "Submarine" -> nHitPoints = Math.min(nHitPoints, 50);
            default -> throw new IllegalStateException("Unexpected value: " + this.getClass().getSimpleName());
        }
    }

    @Override
    public String toString() {
        return " has an attack strength of "+nAttack+ " health bar of "+nHitPoints+
                ", armour of "+ nArmour+ " can be repaired at "+nRepairRate+"HP "+"and prefers attacking " +sPreferredTarget[0];
    }

    public abstract void defendAttack(Ship obAttacker);
}
