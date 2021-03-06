package Schedule;

import UserLogin.Speaker;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * A use case class for storing the values of EventMap from EventManager.
 */

public class EventMapFeatures {
    Room room;
    ArrayList<Speaker> speakers;
    LocalDateTime start;
    LocalDateTime end;

    /**
     *  A room, list of speakers, start time, and end time are required to create an instance of EventFeatures.
     *
     * @param room a Room
     * @param speakers an ArrayList containing speakers
     * @param start the start time
     * @param end the end time
     */

    public EventMapFeatures(Room room, ArrayList<Speaker> speakers, LocalDateTime start, LocalDateTime end){
        this.room =room;
        this.speakers = speakers;
        this.start = start;
        this.end = end;
    }

    /**
     * Returns a room.
     *
     * @return a Room
     */

    public Room getRoom() {
        return room;
    }

    /**
     * Returns a list of speakers.
     *
     * @return an ArrayList containing speakers
     */

    public ArrayList<Speaker> getSpeaker() {
        return speakers;
    }

}
