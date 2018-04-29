package com.keypr.overbooking.dao.impl;

import com.keypr.overbooking.dao.BookingDao;
import com.keypr.overbooking.dao.document.Reservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Kirill Bazarov (es.kelevra@gmail.com)
 *
 * Implementation of {@link BookingDao}
 */
@Component
public class ReservationsDaoImpl implements BookingDao {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public boolean isDateRangeInLimit(int limit, String... days) {
        return mongoTemplate.find(Query.query(Criteria.where("day").in(days).and("limit").gte(limit)),
                Reservation.class).size() == 0;
    }

    @Override
    public void incrementLimitForDateRange(String... days) {
        for (String day : days) {
            mongoTemplate.upsert(Query.query(Criteria.where("day").is(day)),
                    new Update().inc("limit", 1), Reservation.class);
        }
    }

    @Override
    public List<Reservation> findAll() {
        return mongoTemplate.findAll(Reservation.class);
    }
}
