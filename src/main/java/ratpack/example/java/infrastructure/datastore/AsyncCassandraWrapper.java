package ratpack.example.java.infrastructure.datastore;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.ResultSetFuture;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.Statement;
import rx.Observable;
import rx.Scheduler;
import rx.Scheduler.Worker;
import rx.internal.producers.SingleDelayedProducer;

import java.util.concurrent.Executor;

public class AsyncCassandraWrapper {

    public static Observable<ResultSet> executeAsyncOnSubscribe(Statement statement, Scheduler scheduler, Session session) {
        return Observable.create(subscriber -> {

            try {
                ResultSetFuture resultSetFuture = session.executeAsync(statement);

                SingleDelayedProducer<ResultSet> producer = new SingleDelayedProducer<>(subscriber);
                subscriber.setProducer(producer);

                resultSetFuture.addListener(() -> {
                    try {
                        ResultSet result = resultSetFuture.get();
                        producer.setValue(result);
                    } catch (Exception e) {
                        subscriber.onError(new RuntimeException("Error writing to Cassandra", e));
                    }
                }, createExecutor(scheduler));

            } catch (Exception exception) {
                subscriber.onError(new RuntimeException("Error writing to Cassandra", exception));
            }
        });
    }

    private static Executor createExecutor(Scheduler scheduler) {
        return command -> {
            Worker worker = scheduler.createWorker();
            worker.schedule(() -> {
                try {
                    command.run();
                } catch (Exception e) {
                    worker.unsubscribe();
                }
            });
        };
    }

}
