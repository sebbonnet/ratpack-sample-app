package ratpack.example.java.service;

import ratpack.example.java.MyHandler;
import rx.Observable;

/**
 * An example service interface.
 *
 * @see MyHandler
 */
public interface MyService {

    String getValue();

    Observable<String> getValueAsObservable();

    Observable<String> getValueFromHttpClient();

    Observable<String> getValueFromDatabase();
}
