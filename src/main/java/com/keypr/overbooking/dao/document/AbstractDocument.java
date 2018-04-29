package com.keypr.overbooking.dao.document;

import org.springframework.data.annotation.Id;

/**
 * @author Kirill Bazarov (es.kelevra@gmail.com)
 *
 * Abstract document used for mongorepository
 *
 */
public class AbstractDocument {

    @Id
    String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
