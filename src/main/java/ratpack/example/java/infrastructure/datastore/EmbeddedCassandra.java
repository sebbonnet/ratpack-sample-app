package ratpack.example.java.infrastructure.datastore;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.google.common.base.Throwables;
import org.apache.cassandra.service.StorageService;
import org.cassandraunit.utils.EmbeddedCassandraServerHelper;

import java.net.URISyntaxException;

import static com.google.common.base.Preconditions.checkState;

public class EmbeddedCassandra {

    private boolean running;
    private Session session;

    public void start() {
        checkState(!running, "cannot start: already running");
        try {
            EmbeddedCassandraServerHelper.startEmbeddedCassandra("embedded-cassandra.yml", 30000L);
            StorageService.instance.removeShutdownHook();
            running = true;
            session = createSession();
            createKeySpace(session);
        } catch (Exception e) {
            throw Throwables.propagate(e);
        }
    }

    public boolean isRunning() {
        return running;
    }

    private void createKeySpace(Session session) throws URISyntaxException {
        session.execute("CREATE KEYSPACE if not exists ratpack_demo with replication = {'class': 'SimpleStrategy', 'replication_factor': 1}");
        session.execute("use ratpack_demo");
        session.execute("create table my_table(key text primary key, value text)");
        session.execute("insert into my_table(key, value) values('SOME_KEY', 'SOME_VALUE')");
    }

    public int getPort() {
        checkState(running, "cannot get port: not running");
        return EmbeddedCassandraServerHelper.getNativeTransportPort();
    }

    public Session getSession() {
        return session;
    }

    private Session createSession(){
        return Cluster.builder()
                .addContactPoints("localhost")
                .withPort(getPort())
                .build().connect();
    }
}
