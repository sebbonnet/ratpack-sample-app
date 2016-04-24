package ratpack.example.java;

import ratpack.example.java.service.MyHystrixService;
import ratpack.handling.Context;
import ratpack.handling.Handler;
import ratpack.rx.RxRatpack;

import javax.inject.Inject;
import javax.inject.Singleton;

import static java.lang.String.format;

@Singleton
public class DownstreamHttpClientHystrixHandler implements Handler {

    private final MyHystrixService myService;

    @Inject
    public DownstreamHttpClientHystrixHandler(MyHystrixService myService) {
        this.myService = myService;
    }

    @Override
    public void handle(Context ctx) throws Exception {
        RxRatpack.promiseSingle(myService.getValueFromHttpClient())
                .then(result -> ctx.render(format("Response from hystrix http client: %s", result)));
    }
}
