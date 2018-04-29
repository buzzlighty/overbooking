package com.keypr.overbooking.rest.impl;

import com.keypr.overbooking.dto.ConfigDto;
import com.keypr.overbooking.rest.ConfigApi;
import com.keypr.overbooking.service.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author Kirill Bazarov (es.kelevra@gmail.com)
  *
 *
 * Rest controller for app configuration operatios
 */
@RestController
public class ConfigurationController implements ConfigApi {

    @Autowired
    private ConfigurationService configurationService;

    @Override
    @RequestMapping(path = "/configuration", method = RequestMethod.POST)
    public void configure(@RequestBody @Valid ConfigDto configurationDto) {
        configurationService.saveConfiguration(configurationDto);
    }

    @Override
    @RequestMapping(path = "/configuration", method = RequestMethod.GET)
    public ConfigDto configure() {
        return configurationService.getConfiguration();
    }
}
