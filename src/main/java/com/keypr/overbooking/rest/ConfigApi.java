package com.keypr.overbooking.rest;

import com.keypr.overbooking.dto.ConfigDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * @author Kirill Bazarov (es.kelevra@gmail.com)
 */
public interface ConfigApi {

    @ApiOperation(value = "Save configuration", tags = "Configuration")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Configuration has been saved")
    })
    void configure(ConfigDto configurationDto);

    @ApiOperation(value = "Get configuration", tags = "Configuration")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = ConfigDto.class),
            @ApiResponse(code = 404, message = "If configuration is missing")
    })
    ConfigDto configure();
}
