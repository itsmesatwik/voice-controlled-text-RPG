/**
 * A class representing direction objects that contain
 * information on the roomname and the direction it is in wrt
 * a certain room, A room contains multiple Direction objects.
 */
public class Direction {
    /**
     * Name of the direction player can move in.
     */
    private String directionName;
    /**
     * The name of room that this direction leads to.
     */
    private String room;

    public String getDirectionName() {
        return directionName;
    }

    public void setDirectionName(String directionName) {
        this.directionName = directionName;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }
}
