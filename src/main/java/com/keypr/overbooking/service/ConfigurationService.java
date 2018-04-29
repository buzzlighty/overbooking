package com.keypr.overbooking.service;

import com.keypr.overbooking.dto.ConfigurationDto;

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
    void saveConfiguration(ConfigurationDto dto);


    /**
     * Returns saved configuration
     * @return
     */
    ConfigurationDto getConfiguration();
}
