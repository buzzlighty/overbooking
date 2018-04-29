package com.keypr.overbooking.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.keypr.overbooking.dao.document.Config;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author Kirill Bazarov (es.kelevra@gmail.com)
 *
 *
 * Data transfer object for configuration payload
 */
public class ConfigDto {

    @Min(0) @NotNull
    private int rooms;
    @Min(0) @NotNull
    private int overbookingLevel;

    @JsonCreator
    public ConfigDto(
            @JsonProperty("rooms") int rooms,
            @JsonProperty("overbookingLevel")  int overbookingLevel) {
        this.rooms = rooms;
        this.overbookingLevel = overbookingLevel;
    }

    public ConfigDto(Config config) {
        this.rooms = config.getRooms();
        this.overbookingLevel = config.getOverbooking();
    }

    public int getRooms() {
        return rooms;
    }

    public int getOverbookingLevel() {
        return overbookingLevel;
    }

    @Override
    public String toString() {
        return "ConfigDto{" +
                "rooms=" + rooms +
                ", overbookingLevel=" + overbookingLevel +
                '}';
    }
}
