package com.keypr.overbooking.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.keypr.overbooking.dao.document.Config;

import javax.validation.constraints.Min;

/**
 * @author Kirill Bazarov (es.kelevra@gmail.com)
 *
 *
 * Data transfer object for configuration payload
 */
public class ConfigurationDto {

    @Min(0)
    private int rooms;
    @Min(0)
    private int overbookingLevel;

    @JsonCreator
    public ConfigurationDto(
            @JsonProperty("rooms") int rooms,
            @JsonProperty("overbookingLevel")  int overbookingLevel) {
        this.rooms = rooms;
        this.overbookingLevel = overbookingLevel;
    }

    public ConfigurationDto(Config config) {
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
        return "ConfigurationDto{" +
                "rooms=" + rooms +
                ", overbookingLevel=" + overbookingLevel +
                '}';
    }
}
