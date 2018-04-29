package com.keypr.overbooking.dao;

import com.keypr.overbooking.dao.document.Config;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author Kirill Bazarov (es.kelevra@gmail.com)
 *
 * Repository for {@link Config}
 *
 */
public interface ConfigRepository extends MongoRepository<Config, String> {

    String ID = "00000000-0000-4000-0000-000000000001";

}
