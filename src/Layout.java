import java.util.ArrayList;

/**
 * Map object containing
 */
public class Layout {
    /**
     * Starting room of the map.
     */
    private String startingRoom;
    /**
     * Ending room of the map.
     */
    private String  endingRoom;
    /**
     * list of rooms in the map.
     */
    private ArrayList<Room> rooms;

    private ArrayList<Monster> monsters;

    private Player player;

    public String getStartingRoom() {
        return startingRoom;
    }

    public String getEndingRoom() {
        return endingRoom;
    }

    public ArrayList<Room> getRooms() {
        return rooms;
    }

    public ArrayList<Monster> getMonsters() {
        return monsters;
    }

    /**
     * getting the room object for the room a direction leads to.
     * @param roomName the direction object
     * @return returns the room object correlating to the direction
     */
    public Room getRoomFromName (String roomName) {
        for (Room room : this.rooms) {
            if (room.getName().equalsIgnoreCase(roomName)) {
                return room;
            }
        }
        return null;
    }

    public Monster getMonsterFromName(String monsterName) {
        for (Monster monster : monsters) {
            if (monster.getName().equalsIgnoreCase(monsterName)) {
                return monster;
            }
        }
        return null;
    }

    public Player getPlayer() {
        return this.player;
    }

    Layout() {
        startingRoom = "";
        endingRoom = "";
        rooms = new ArrayList<>();
        monsters = new ArrayList<>();
        player = new Player();
    }
}

