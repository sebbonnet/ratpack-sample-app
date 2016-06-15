package ratpack.example.java;

import com.datastax.driver.core.Session;
import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import org.apache.http.nio.client.HttpAsyncClient;
import ratpack.example.java.infrastructure.datastore.EmbeddedCassandra;
import ratpack.example.java.infrastructure.datastore.MyTableRepository;
import ratpack.example.java.infrastructure.httpclient.HttpAsyncClientFactory;
import ratpack.example.java.service.MyHystrixService;
import ratpack.example.java.service.MyHystrixServiceImpl;
import ratpack.example.java.service.MyService;
import ratpack.example.java.service.MyServiceImpl;
import ratpack.handling.HandlerDecorator;

/**
 * An example Guice module.
 */
public class MyModule extends AbstractModule {

    /**
     * Adds a service impl to the application, and registers a decorator so that all requests are logged.
     * Registered implementations of {@link ratpack.handling.HandlerDecorator} are able to decorate the application handler.
     *
     * @see MyHandler
     */
    protected void configure() {
        bind(MyService.class).to(MyServiceImpl.class);
        bind(MyHystrixService.class).to(MyHystrixServiceImpl.class);
        bind(HttpAsyncClient.class).toInstance(HttpAsyncClientFactory.createHttpClient());
        bind(Session.class).toInstance(getCassandraSession());
        bind(MyTableRepository.class);

        bind(MyHandler.class);
        bind(FixedValueObservableHandler.class);
        bind(DownstreamHttpClientHandler.class);
        bind(DownstreamHttpClientHystrixHandler.class);
        bind(DownstreamCassandraHandler.class);

        Multibinder.newSetBinder(binder(), HandlerDecorator.class).addBinding().toInstance(HandlerDecorator.prepend(new LoggingHandler()));
    }

    private Session getCassandraSession() {
        EmbeddedCassandra embeddedCassandra = new EmbeddedCassandra();
        embeddedCassandra.start();
        return embeddedCassandra.getSession();
    }

}
