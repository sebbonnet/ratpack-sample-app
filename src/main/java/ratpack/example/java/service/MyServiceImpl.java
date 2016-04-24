package ratpack.example.java.service;

import com.datastax.driver.core.Session;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import ratpack.example.java.MyHandler;
import ratpack.example.java.infrastructure.datastore.DummyValueRepository;
import ratpack.example.java.infrastructure.httpclient.AsyncClientObservableWrapper;
import ratpack.rx.RxRatpack;
import rx.Observable;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.net.URI;
import java.util.Date;

import static java.lang.String.format;

/**
 * The service implementation.
 *
 * @see MyHandler
 */
@Singleton
public class MyServiceImpl implements MyService {

    private final AsyncClientObservableWrapper asyncHttpClient;
    private final Session session;

    @Inject
    public MyServiceImpl(AsyncClientObservableWrapper asyncHttpClient, Session session) {
        this.asyncHttpClient = asyncHttpClient;
        this.session = session;
    }

    @Override
    public String getValue() {
        sleepFor(5000);
        return format("current date: %s", new Date());
    }

    @Override
    public Observable<String> getValueAsObservable() {
        return Observable.create((Observable.OnSubscribe<String>) subscriber -> {
            subscriber.onNext(getValue());
            subscriber.onCompleted();
        });
    }

    @Override
    public Observable<String> getValueFromHttpClient() {
        HttpUriRequest request = RequestBuilder.get(URI.create("http://google.com")).build();
        return asyncHttpClient.sendAsyncRequest(request)
                .map(httpResponse -> {
                    sleepFor(5000);
                    return format("%s. Status: %s", new Date(), httpResponse.getStatusLine().toString());
                });
    }

    @Override
    public Observable<String> getValueFromDatabase() {
        return new DummyValueRepository(session).getValue(RxRatpack.scheduler());
    }

    private void sleepFor(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException("Unable to sleep for this long...", e);
        }
    }

}
