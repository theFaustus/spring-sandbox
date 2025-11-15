package evil.inc.data.repo;

import evil.inc.data.model.Person;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.Collection;

//@Repository
//public class BadPersonRepositoryImpl implements BadPersonRepository {
//
//    private final JdbcClient jdbc;
//
//    public BadPersonRepositoryImpl(JdbcClient jdbc) {
//        this.jdbc = jdbc;
//    }
//
//    @Override
//    public Collection<Person> findAll() {
//        return jdbc.sql("select * from person")
//                .query((rs, rowNum) -> new Person(rs.getLong("id"), rs.getString("name")))
//                .list();
//    }
//}
