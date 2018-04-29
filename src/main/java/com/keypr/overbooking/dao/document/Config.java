package com.keypr.overbooking.dao.document;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author Kirill Bazarov (es.kelevra@gmail.com)
 *
 * Document for configuration collection
 */
@Document(collection = "config")
public class Config extends AbstractDocument {

    private int rooms;
    private int overbooking;

    public Config(int rooms, int overbooking) {
        this.rooms = rooms;
        this.overbooking = overbooking;
    }

    public int getRooms() {
        return rooms;
    }

    public int getOverbooking() {
        return overbooking;
    }

    @Override
    public String toString() {
        return "Config{" +
                "rooms=" + rooms +
                ", overbooking=" + overbooking +
                ", id='" + id + '\'' +
                '}';
    }
}
