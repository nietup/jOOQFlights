package mgr.flights.jooq;

import mgr.flights.jooq.tables.City;
import mgr.flights.jooq.tables.daos.CityDao;
import mgr.flights.jooq.tables.records.CityRecord;
import org.jooq.DSLContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.ThreadLocalRandom;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CityTests {

    @Autowired
    private DSLContext create;

    @Autowired
    private CityDao cityDao;

    @Test
    public void jOOQTableTest() {
        System.out.println(create.select(City.CITY.NAME).from(City.CITY).fetch());
    }

    @Test
    public void jOOQDaoTest() {
        System.out.println(cityDao.findAll());
    }

    @Test
    public void insertTest() {
        mgr.flights.jooq.tables.pojos.City city = new mgr.flights.jooq.tables.pojos.City();
        city.setName(randomName());

        CityRecord cityRecord = create.newRecord(City.CITY, city);
        cityRecord.store();
        System.out.println(create.select(City.CITY.NAME).from(City.CITY).fetch());
    }

    @Test
    public void updateRecordTest() {
        CityRecord city = create
                .selectFrom(City.CITY)
                .where(City.CITY.CITY_ID.eq(16))
                .fetchAny();

        city.setName(city.getName() + "a");
        city.store();

        System.out.println(create.select(City.CITY.NAME).from(City.CITY).fetch());
    }

    @Test
    public void updatePojoTest() {
        mgr.flights.jooq.tables.pojos.City city = cityDao.fetchOneByCityId(16);

        city.setName(city.getName() + "b");
        cityDao.update(city);

        System.out.println(create.select(City.CITY.NAME).from(City.CITY).fetch());
    }

    private String randomName() {
        String[] vowels = new String[]{"a", "i", "u", "e", "o" };
        String[] consonants = new String[]{"w", "r", "t", "p", "s", "d", "f", "g", "h", "k", "l", "z", "x", "c", "v", "b", "n", "m" };

        StringBuilder result = new StringBuilder(vowels[ThreadLocalRandom.current().nextInt(0, 5)].toUpperCase());
        for (int i = 0; i < 4; i++) {
            result.append(consonants[ThreadLocalRandom.current().nextInt(0, 18)]);
            result.append(vowels[ThreadLocalRandom.current().nextInt(0, 5)]);
        }

        return result.toString();
    }
}
