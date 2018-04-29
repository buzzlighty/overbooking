package com.keypr.overbooking.service;

import com.keypr.overbooking.dto.ConfigDto;

/**
 * @author Kirill Bazarov (es.kelevra@gmail.com)
 *
 * Configration service
 */
public interface ConfigurationService {

    /**
     * Saves configuration
     *
     */
    void saveConfiguration(ConfigDto dto);


    /**
     * Returns saved configuration
     * @return
     */
    ConfigDto getConfiguration();
}
