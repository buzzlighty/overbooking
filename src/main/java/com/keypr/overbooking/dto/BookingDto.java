package com.keypr.overbooking.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * @author Kirill Bazarov (es.kelevra@gmail.com)
 *
 *
 * Data transfer object for booking payload
 */
public class BookingDto {

    @NotNull
    private String clientName;
    @NotNull
    private String clientEmail;
    @NotNull
    private LocalDate arrivalDate;
    @NotNull
    private LocalDate departureDate;

    @JsonCreator
    public BookingDto(
            @JsonProperty("clientName") String clientName,
            @JsonProperty("clientEmail") String clientEmail,
            @JsonProperty("arrivalDate") @JsonFormat(pattern="dd.MM.yyyy") LocalDate arrivalDate,
            @JsonProperty("departureDate") @JsonFormat(pattern="dd.MM.yyyy") LocalDate departureDate) {
        this.clientName = clientName;
        this.clientEmail = clientEmail;
        this.arrivalDate = arrivalDate;
        this.departureDate = departureDate;
    }

    public String getClientName() {
        return clientName;
    }

    public String getClientEmail() {
        return clientEmail;
    }

    @ApiModelProperty(example = "10.05.2018")
    public LocalDate getArrivalDate() {
        return arrivalDate;
    }

    @ApiModelProperty(example = "15.05.2018")
    public LocalDate getDepartureDate() {
        return departureDate;
    }

    @Override
    public String toString() {
        return "BookingDto{" +
                "clientName='" + clientName + '\'' +
                ", clientEmail='" + clientEmail + '\'' +
                ", arrivalDate=" + arrivalDate +
                ", departureDate=" + departureDate +
                '}';
    }
}
