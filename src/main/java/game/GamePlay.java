package game;

import java.util.*;

public class GamePlay {
  private static final ArrayList<Ship> player1Fleet = new ArrayList<>();
  private static final ArrayList<Ship> player2Fleet = new ArrayList<>();
  private static final Random random = new Random();
  ;
  private static final Map<Ship, Integer> mostDestructiveShipByDamageMap = new HashMap<>();
  private static String mostDestructiveShipByHitPoint = null;
  private static Integer mostDestructiveShipByHitPointPoint = 0;

  public static void main(String[] args) {
    battleRound();
  }

  private static void createFleets() {

    for (int i = 0; i < 10; i++) {
      player1Fleet.add(new BattleShip("Player1 BattleShip " + (i + 1), ShipType.BATTLESHIP));
      player2Fleet.add(new BattleShip("Player2 BattleShip " + (i + 1), ShipType.BATTLESHIP));
    }

    for (int i = 0; i < 5; i++) {
      player1Fleet.add(new Carrier("Player1 Carrier " + (i + 1), ShipType.CARRIER));
      player2Fleet.add(new Carrier("Player2 Carrier " + (i + 1), ShipType.CARRIER));
    }

    for (int i = 0; i < 5; i++) {
      player1Fleet.add(new Destroyer("Player1 Destroyer " + (i + 1), ShipType.DESTROYER));
      player2Fleet.add(new Destroyer("Player2 Destroyer " + (i + 1), ShipType.DESTROYER));
    }

    for (int i = 0; i < 5; i++) {
      player1Fleet.add(new Submarine("Player1 Submarine " + (i + 1), ShipType.SUBMARINE));
      player2Fleet.add(new Submarine("Player2 Submarine " + (i + 1), ShipType.SUBMARINE));
    }

    for (int i = 0; i < 20; i++) {
      player1Fleet.add(new PatrolBoat("Player1 PatrolBoat " + (i + 1), ShipType.PATROLBOAT));
      player2Fleet.add(new PatrolBoat("Player2 PatrolBoat " + (i + 1), ShipType.PATROLBOAT));
    }
  }

  private static void repairShips(ArrayList<Ship> fleet, Set<Ship> damagedShips) {
    for (Ship ship : fleet) {
      if (damagedShips.contains(ship) && !ship.isDestroyed()) {
        ship.repair();
      }
    }
  }

  private static void attack() {
    Set<Ship> damagedShips = new HashSet<>();

    for (Ship attacker : player1Fleet) {
      if (!attacker.isDestroyed()) {
        Ship target = player2Fleet.get(random.nextInt(player2Fleet.size()));
        attacker.defendAttack(target);
        if (target.isDestroyed()) {
          damagedShips.add(target);
        }
        updateDamageMap(target, Math.max(0, attacker.getAttack() - target.getArmour()));
      }
    }

    for (Ship attacker : player2Fleet) {
      if (!attacker.isDestroyed()) {
        Ship target = player1Fleet.get(random.nextInt(player1Fleet.size()));
        attacker.defendAttack(target);
        if (target.isDestroyed()) {
          damagedShips.add(target);
        }
        updateDamageMap(target, Math.max(0, attacker.getAttack() - target.getArmour()));
      }
    }

    repairShips(player1Fleet, damagedShips);
    repairShips(player2Fleet, damagedShips);
  }

  public static void battleRound() {
    createFleets();
    int round = 0;

    while (Boolean.TRUE.equals(!isPlayer1FleetDestroyed())
        && Boolean.TRUE.equals(!isPlayer2FleetDestroyed())) {
      round++;
      System.out.println("Round " + round);

      displayFleetStatus("Player 1 Fleet:", player1Fleet);
      displayFleetStatus("Player 2 Fleet:", player2Fleet);

      attack();

      displayFleetStatus("Player 1 Fleet after Round " + round + ":", player1Fleet);
      displayFleetStatus("Player 2 Fleet after Round " + round + ":", player2Fleet);
    }

    displayWinner();
    displayFinishRound(round);
    displayRemainingShipsWithHitPoint();
    displayShipsDestroyedInformation();
    displayMvpMostDestructiveShipByHitPoint();
    displayMvpMostDestructiveShip();
  }

  private static void displayWinner() {
    System.out.println("\n\n********** WINNER ********");
    String winner = determineWinner();
    if ("It's a draw!".equalsIgnoreCase(winner)) {
      System.out.println("Maximum rounds reached. The game is a draw.");
    } else {
      System.out.println("Game over. " + winner + " is the winner!");
    }
  }

  private static void displayFinishRound(int round) {
    System.out.println("\n\n****** Game Completion round ******");
    System.out.println("Finished the game at round " + round);
  }

  private static void displayRemainingShipsWithHitPoint() {
    System.out.println("\n\n********** Ships Remaining ************");
    String winner = determineWinner();
    if ("Player 1".equalsIgnoreCase(winner)) {
      displayFleetStatus(player1Fleet);
    } else if ("Player ".equalsIgnoreCase(winner)) {
      displayFleetStatus(player2Fleet);
    }
  }

  private static String determineWinner() {

    int totalPlayer1Health = player1Fleet.stream().mapToInt(ship -> ship.nHitPoints).sum();
    int totalPlayer2Health = player2Fleet.stream().mapToInt(ship -> ship.nHitPoints).sum();

    if (totalPlayer1Health > totalPlayer2Health) {
      return "Player 1";
    } else if (totalPlayer2Health > totalPlayer1Health) {
      return "Player 2";
    } else {
      return "It's a draw!";
    }
  }

  private static void displayFleetStatus(String header, ArrayList<Ship> fleet) {
    System.out.println(header);
    for (Ship ship : fleet) {
      displayShipStatus(ship);
    }
  }

  private static void displayFleetStatus(ArrayList<Ship> fleet) {
    for (Ship ship : fleet) {
      displayShipStatus(ship);
    }
  }

  private static final String HP_LABEL = " - HP: ";

  private static void displayShipStatus(Ship ship) {
    int health = Math.max(0, ship.nHitPoints);
    System.out.println(getShipName(ship) + HP_LABEL + health);
  }

  public static String getShipName(Ship ship) {
    String shipName = null;
    if (ship instanceof BattleShip battleship) {
      shipName = battleship.getName();
    } else if (ship instanceof Carrier carrier) {
      shipName = carrier.getName();
    } else if (ship instanceof Destroyer destroyer) {
      shipName = destroyer.getName();
    } else if (ship instanceof Submarine submarine) {
      shipName = submarine.getName();
    } else if (ship instanceof PatrolBoat patrolBoat) {
      shipName = patrolBoat.getName();
    }
    return shipName;
  }

  private static void displayShipsDestroyedInformation() {
    System.out.println(
        "\n\n******** Ships destroyed and what opponent ship destroyed it **********");
    Map<Ship, String> destroyedShips = new HashMap<>();
    for (Ship ship : player1Fleet) {
      if (ship.isDestroyed() && ship.destroyedBy != null) {
        destroyedShips.put(ship, ship.destroyedBy);
      }
    }

    for (Ship ship : player2Fleet) {
      if (ship.isDestroyed() && ship.destroyedBy != null) {
        destroyedShips.put(ship, ship.destroyedBy);
      }
    }

    for (Map.Entry<Ship, String> entry : destroyedShips.entrySet()) {
      System.out.println(getShipName(entry.getKey()) + " destroyed by " + entry.getValue());
    }
  }

  private static void displayMvpMostDestructiveShipByHitPoint() {
    System.out.println(
        "\n\n******** MVP ship that inflicted the most hit point damage ************");
    calculateHitPointMVP();
    System.out.println(
        "MVP for hit point damage is: "
            + mostDestructiveShipByHitPoint
            + " with a damage level of "
            + mostDestructiveShipByHitPointPoint);
  }

  private static void displayMvpMostDestructiveShip() {
    System.out.println("\n\n******** MVP ship that destroyed the most opposing ships ************");
    HashMap<Ship, Integer> mostDestructiveShip = findMvpMostDestructiveShip();
    for (Map.Entry<Ship, Integer> entry : mostDestructiveShip.entrySet()) {
      System.out.println(
          getShipName(entry.getKey())
              + " destroyed a total of  "
              + entry.getValue()
              + " from the opposing side");
    }
  }

  private static HashMap<Ship, Integer> findMvpMostDestructiveShip() {
    HashMap<Ship, Integer> mostDestructiveShip = new HashMap<>();
    Ship player1MVPByDestroyed = null;
    int player1MaxDestroyedCount = Integer.MIN_VALUE;

    for (Ship player1Ship : player1Fleet) {
      int destroyedCount = 0;

      for (Ship player2Ship : player2Fleet) {
        if (player2Ship.destroyedBy != null
            && player2Ship.destroyedBy.equalsIgnoreCase(getShipName(player1Ship))) {
          destroyedCount++;
        }
      }

      if (destroyedCount > player1MaxDestroyedCount) {
        player1MaxDestroyedCount = destroyedCount;
        player1MVPByDestroyed = player1Ship;
      }
    }

    Ship player2MVPByDestroyed = null;
    int player2MaxDestroyedCount = Integer.MIN_VALUE;

    for (Ship player2Ship : player1Fleet) {
      int destroyedCount = 0;

      for (Ship player1Ship : player1Fleet) {
        if (player1Ship.destroyedBy != null
            && player1Ship.destroyedBy.equalsIgnoreCase(getShipName(player2Ship))) {
          destroyedCount++;
        }
      }

      if (destroyedCount > player2MaxDestroyedCount) {
        player2MaxDestroyedCount = destroyedCount;
        player2MVPByDestroyed = player2Ship;
      }
    }

    if (player2MaxDestroyedCount > player1MaxDestroyedCount) {
      mostDestructiveShip.put(player2MVPByDestroyed, player2MaxDestroyedCount);
    } else if (player2MaxDestroyedCount < player1MaxDestroyedCount) {
      mostDestructiveShip.put(player1MVPByDestroyed, player1MaxDestroyedCount);
    } else {
      mostDestructiveShip.put(player2MVPByDestroyed, player2MaxDestroyedCount);
    }
    return mostDestructiveShip;
  }

  private static void calculateHitPointMVP() {

    for (Ship ship : player1Fleet) {
      int damage = mostDestructiveShipByDamageMap.getOrDefault(ship, 0);
      if (damage > mostDestructiveShipByHitPointPoint) {
        mostDestructiveShipByHitPointPoint = damage;
        mostDestructiveShipByHitPoint = ship.toString();
      }
    }

    for (Ship ship : player2Fleet) {
      int damage = mostDestructiveShipByDamageMap.getOrDefault(ship, 0);
      if (damage > mostDestructiveShipByHitPointPoint) {
        mostDestructiveShipByHitPointPoint = damage;
        mostDestructiveShipByHitPoint = ship.toString();
      }
    }
  }

  private static void updateDamageMap(Ship ship, int damage) {
    mostDestructiveShipByDamageMap.put(
        ship, mostDestructiveShipByDamageMap.getOrDefault(ship, 0) + damage);
  }

  private static Boolean isPlayer1FleetDestroyed() {
    int destroyedShips = 0;
    for (Ship player1Ship : player1Fleet) {
      if (player1Ship.isDestroyed()) {
        destroyedShips = destroyedShips + 1;
      }
    }
    if (player1Fleet.size() == destroyedShips) {
      return true;
    }
    return false;
  }

  private static Boolean isPlayer2FleetDestroyed() {
    int destroyedShips = 0;
    for (Ship player2Ship : player2Fleet) {
      if (player2Ship.isDestroyed()) {
        destroyedShips = destroyedShips + 1;
      }
    }
    if (player2Fleet.size() == destroyedShips) {
      return true;
    }
    return false;
  }
}
