package com.keypr.overbooking.service.impl;

import com.keypr.overbooking.dao.ConfigRepository;
import com.keypr.overbooking.dao.document.Config;
import com.keypr.overbooking.dto.ConfigDto;
import com.keypr.overbooking.exception.MissingConfigurationException;
import com.keypr.overbooking.lock.ConfigurationLockProvider;
import com.keypr.overbooking.service.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.locks.Lock;

/**
 * @author Kirill Bazarov (es.kelevra@gmail.com)
 *
 * Implemenation of {@link ConfigurationService}
 *
 */
@Service
public class ConfigurationServiceImpl implements ConfigurationService {

    @Autowired
    private ConfigRepository configRepository;

    @Autowired
    private ConfigurationLockProvider configurationLockProvider;

    /**
     * Obtains write lock for settings, so no-one can read it and updates configuration with new values
     *
     */
    @Override
    public void saveConfiguration(ConfigDto configurationDto) {
        Config config = new Config(configurationDto.getRooms(), configurationDto.getOverbookingLevel());

        config.setId(ConfigRepository.ID);

        Lock lock = configurationLockProvider.getConfigurationLock().writeLock();

        try {
            lock.lock();

            configRepository.save(config);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public ConfigDto getConfiguration() {

        Lock lock = configurationLockProvider.getConfigurationLock().readLock();
        try {
            lock.lock();

            Config config = configRepository.findById(ConfigRepository.ID).orElseThrow(MissingConfigurationException::new);

            return new ConfigDto(config.getRooms(), config.getOverbooking());
        } finally {
            lock.unlock();
        }
    }
}
