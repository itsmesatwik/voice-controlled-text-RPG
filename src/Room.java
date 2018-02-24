import java.util.ArrayList;

/**
 * Class for a room object containing the name, description, available directions and the items.
 */
public class Room {
    /**
     * Name of the room object.
     */
    private String name;
    /**
     * Description of the room object.
     */
    private String description;
    /**
     * Directions a player can move from this room.
     */
    private ArrayList<Direction> directions;
    /**
     * List of Items in the room.
     */
    private ArrayList<Item> items;

    /**
     * An arraylist of all the monsters in the room
     */
    private ArrayList<String> monstersInRoom;

    Room() {
        name = "";
        description = "";
        directions = new ArrayList<>();
        items = new ArrayList<>();
        monstersInRoom = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<Direction> getDirections() {
        return directions;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public ArrayList<String> getMonstersInRoom() {
        return monstersInRoom;
    }

    public void printItems() {

        if (items.size() == 0) {
            System.out.println("This room contains nothing");
        }

        else {
            System.out.print("This room contains ");
            for (Item item : items) {
                if (items.indexOf(item) == items.size() - 1) {
                    System.out.print(item.getName() + ".");
                }
                else if (items.indexOf(item) == items.size() - 2) {
                    System.out.print(item.getName() + " and ");
                }
                else {
                    System.out.print(item.getName() + ", ");
                }
            }
            System.out.println("");
        }
    }

    public void printDirections() {
        if (directions.size() == 0) {
            System.out.println("Nowhere to go from here");
        }
        else if (directions.size() == 1) {
            System.out.println("From here, you can go: " + directions.get(0).getDirectionName());
        }
        else {
            System.out.print("From here, you can go: ");
            for (int i = 0; i < directions.size() - 1; i++) {
                System.out.print(directions.get(i).getDirectionName() + ", ");
            }
            System.out.println("or " + directions.get(directions.size() - 1).getDirectionName());
        }
    }

    public void printMonsters() {
        if (monstersInRoom.size() == 0) {
            System.out.println("There are no monsters in the room.");
        }
        else if (monstersInRoom.size() == 1) {
            System.out.println("This room contains the monster " + monstersInRoom.get(0));
        }
        else {
            System.out.println("This room contains monsters: ");
            for (int i = 0; i < monstersInRoom.size() - 1; i++) {
                System.out.print(monstersInRoom.get(i) + ", ");
            }
            System.out.println("and " + monstersInRoom.get(monstersInRoom.size() - 1));
        }
    }

   /* public ArrayList<Room> neighbours(Layout map) {
        ArrayList<Room> neighbours = new ArrayList<>();
        for (Direction dir : directions) {
            neighbours.add(map.getRoomFromDirection(dir));
        }
        return neighbours;
    }

    public ArrayList<String> neighbourNames (Layout map) {
        ArrayList<String> neighbourNames = new ArrayList<>();
        for (Direction dir : directions) {
            neighbourNames.add(dir.getRoom());
        }
        return neighbourNames;
    }*/

    public Item getItemFromName(String itemName) {
        for (Item items : this.getItems()) {
            if (items.getName().equalsIgnoreCase(itemName)) {
                return items;
            }
        }
        return null;
    }

}
