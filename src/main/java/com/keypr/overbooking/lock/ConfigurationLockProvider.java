package com.keypr.overbooking.lock;

import java.util.concurrent.locks.ReadWriteLock;

/**
 * @author Kirill Bazarov (es.kelevra@gmail.com)
 *
 */
public interface ConfigurationLockProvider {

    /**
     * Returns read write lock for settings (limit, rooms)
     *
     *
     */
    ReadWriteLock getConfigurationLock();

}
