import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Player class, contains information about player and the items it contains and the current room.
 */
public class Player {
    private static final int LEVEL_ONE_EXP = 25;
    private static final int LEVEL_TWO_EXP = 50;
    private static final int EXP_MULTIPLIER = 11;
    private String name;
    @SerializedName("items") private ArrayList<Item> playerInventory;
    @SerializedName("health") private double remainingHealth;
    private double attack, defence, healthPoints;
    private int level;
    private Room currentRoom;
    private boolean isDueling;
    private double experiencePoints;

    public Player() {
    }

    public void addExperiencePoints(double newExperiencePoints) {
        this.experiencePoints += newExperiencePoints;
    }

    public void setRemainingHealth(double newHealth) {
        this.remainingHealth = newHealth;
    }

    public String getName() {
        return name;
    }

    public double getAttack() {
        return attack;
    }

    public double getDefence() {
        return defence;
    }

    public double getRemainingHealth() {
        return remainingHealth;
    }

    public int getLevel() {
        return level;
    }
    public boolean isDueling() {
        return isDueling;
    }

    public void setDueling(boolean duelStatus) {
        this.isDueling = duelStatus;
    }

    /**
     * Constructor for the player using the layout
     * @param playerMap the map on which the player is in
     */
    Player(Layout playerMap) {
        playerInventory = new ArrayList<>();
        for (Room room : playerMap.getRooms()) {
            if (room.getName().equals(playerMap.getStartingRoom())) {
                currentRoom = room;
            }
        }
        isDueling = false;
    }

    public ArrayList<Item> getPlayerInventory() {
        return playerInventory;
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }

    public void setCurrentRoom(Room currentRoom) {
        this.currentRoom = currentRoom;
    }

    public void addItem(Item item) {
        if (item == null) {
            System.out.println("Item not found");
            return;
        }
        else if (item.getName().length() == 0){
            throw new IllegalArgumentException(ERROR_CONSTANTS.EMPTY_STRING_EXCEPTION);
        }
        if (currentRoom.getItems().contains(item)) {
            this.playerInventory.add(item);
            currentRoom.getItems().remove(currentRoom.getItems().indexOf(item));
        }
        else {
            System.out.println("I can't take " + item);
        }
    }

    /**
     * remove an item from inventory
     * @param item item to remove
     */
    public void removeItem(Item item) {
        if (item == null) {
            System.out.println("Item not found in Inventory");
            return;
        }
        else if (item.getName().length() == 0){
            throw new IllegalArgumentException(ERROR_CONSTANTS.EMPTY_STRING_EXCEPTION);
        }
        if (playerInventory.contains(item)) {
            playerInventory.remove(item);
            currentRoom.getItems().add(item);
        }
        else {
            System.out.println("I can't drop " + item);
        }
    }

    /**
     * stats
     */
    public void showStats() {
        System.out.println("Your stats are:");
        System.out.println("Attack: " + this.attack);
        System.out.println("Defence: " + this.defence);
        System.out.println("Health: " + this.remainingHealth);
        System.out.println("Experience: " + this.experiencePoints);
        System.out.println("Level: " + this.level);
        this.listPlayerItems();
    }

    /**
     * lists player inventory
     */
    public void listPlayerItems() {
        System.out.print("You are carrying ");
        int inventorySize = playerInventory.size();
        if (inventorySize == 0) {
            System.out.println("nothing.");
        }
        else {
            for (Item item : playerInventory) {
                if (playerInventory.indexOf(item) == inventorySize - 1) {
                    System.out.println(item.getName() + ".");
                    break;
                }
                if (playerInventory.indexOf(item) == inventorySize - 2) {
                    System.out.print(item.getName() + " and ");
                }
                else {
                    System.out.print(item.getName() + ", ");
                }
            }
        }
    }

    /**
     * get item from name
     * @param itemName item name
     * @return return an item
     */
    public Item getItemFromNamePl(String itemName) {
        for (Item items : this.getPlayerInventory()) {
            if (items.getName().equalsIgnoreCase(itemName)) {
                return items;
            }
        }
        return null;
    }

    public double getHealthPoints() {
        return healthPoints;
    }

    public void setHealthPoints(double healthPoints) {
        this.healthPoints = healthPoints;
    }

    /**
     * experience required for levelling up
     * @param level level we want to check for
     * @return return the number of points
     */
    public int experienceRequired(int level) {
        if (level == 1) {
            return LEVEL_ONE_EXP;
        }
        if (level == 2) {
            return LEVEL_TWO_EXP;
        }
        else {
            return (experienceRequired(level - 1) + experienceRequired(level - 2))*EXP_MULTIPLIER;
        }
    }

    public double getExperiencePoints() {
        return experiencePoints;
    }

    /**
     * function of leveling up
     */
    public void levelUp() {
        experiencePoints = experiencePoints - experienceRequired(this.level + 1);
        this.remainingHealth *= 1.3;
        this.attack *= 1.5;
        this.defence *= 1.5;
        this.healthPoints = remainingHealth;
        level++;
        System.out.println("Wow! You leveled up to : Level " + level);
    }
}
