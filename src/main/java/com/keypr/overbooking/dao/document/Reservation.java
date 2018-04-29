package com.keypr.overbooking.dao.document;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author Kirill Bazarov (es.kelevra@gmail.com)
 *
 * Document for reservations collection
 */
@Document(collection = "reservations")
public class Reservation extends AbstractDocument{

    private String day;
    private int limit;

    public Reservation(String id, String day, int limit) {
        this.id = id;
        this.day = day;
        this.limit = limit;
    }

    public String getId() {
        return id;
    }

    public String getDay() {
        return day;
    }

    public int getLimit() {
        return limit;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id='" + id + '\'' +
                ", day='" + day + '\'' +
                ", limit=" + limit +
                '}';
    }
}
