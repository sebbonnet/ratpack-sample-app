package ratpack.example.java.infrastructure.datastore;

import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.SimpleStatement;
import rx.Observable;
import rx.Scheduler;

import javax.inject.Inject;
import javax.inject.Singleton;

import static ratpack.example.java.infrastructure.datastore.AsyncCassandraWrapper.executeAsyncOnSubscribe;

@Singleton
public class MyTableRepository {
    private final Session session;

    @Inject
    public MyTableRepository(Session session) {
        this.session = session;
    }

    public Observable<String> getValue(Scheduler scheduler) {
        return executeAsyncOnSubscribe(new SimpleStatement("select key,value from my_table"), scheduler, session)
                .map(rows -> {
                    sleepFor(5000);
                    Row row = rows.one();
                    return row.getString("value");
                });
    }

    private void sleepFor(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException("Unable to sleep for this long...", e);
        }
    }
}
