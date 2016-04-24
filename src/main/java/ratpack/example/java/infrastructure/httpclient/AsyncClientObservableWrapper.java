package ratpack.example.java.infrastructure.httpclient;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.nio.client.HttpAsyncClient;
import ratpack.rx.RxRatpack;
import rx.Observable;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AsyncClientObservableWrapper {

    private HttpAsyncClient client;

    @Inject
    public AsyncClientObservableWrapper(HttpAsyncClient client) {
        this.client = client;
    }

    public Observable<HttpResponse> sendAsyncRequest(HttpUriRequest request) {

        return RxRatpack.observe(RxRatpack.promiseSingle(subscriber ->
        {
            this.client.execute(request, new FutureCallback<HttpResponse>() {
                @Override
                public void completed(HttpResponse result) {
                    subscriber.onNext(result);
                    subscriber.onCompleted();
                }

                @Override
                public void failed(Exception ex) {
                    subscriber.onError(ex);
                }

                @Override
                public void cancelled() {
                    subscriber.onCompleted();
                }
            });
        }));
    }
}
