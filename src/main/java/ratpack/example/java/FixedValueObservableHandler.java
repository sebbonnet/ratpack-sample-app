package ratpack.example.java;

import ratpack.example.java.service.MyService;
import ratpack.handling.Context;
import ratpack.handling.Handler;
import ratpack.rx.RxRatpack;

import javax.inject.Inject;
import javax.inject.Singleton;

import static java.lang.String.format;

@Singleton
public class FixedValueObservableHandler implements Handler {

    private final MyService service;

    @Inject
    public FixedValueObservableHandler(MyService service) {
        this.service = service;
    }

    @Override
    public void handle(Context ctx) throws Exception {
        RxRatpack.promiseSingle(service.getValueAsObservable())
                .then(result -> ctx.render(format("Response from observable: %s", result)));
    }
}
