package com.keypr.overbooking.rest;

import com.keypr.overbooking.dto.ConfigurationDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * @author kyrylo.bazarov@avid.com
 */
public interface ConfigApi {

    @ApiOperation(value = "Save configuration", tags = "Configuration")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Configuration has been saved")
    })
    void configure(ConfigurationDto configurationDto);

    @ApiOperation(value = "Get configuration", tags = "Configuration")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = ConfigurationDto.class),
            @ApiResponse(code = 404, message = "If configuration is missing")
    })
    ConfigurationDto configure();
}
