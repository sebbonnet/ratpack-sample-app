package ratpack.example.java.service;

import rx.Observable;

public interface MyHystrixService {
    Observable<String> getValueFromHttpClient();
}
