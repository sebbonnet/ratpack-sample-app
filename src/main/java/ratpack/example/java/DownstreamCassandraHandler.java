package ratpack.example.java;

import ratpack.example.java.service.MyService;
import ratpack.handling.Context;
import ratpack.handling.Handler;
import ratpack.rx.RxRatpack;

import javax.inject.Inject;
import javax.inject.Singleton;

import static java.lang.String.format;

@Singleton
public class DownstreamCassandraHandler implements Handler {

    private final MyService myService;

    @Inject
    public DownstreamCassandraHandler(MyService myService) {
        this.myService = myService;
    }

    @Override
    public void handle(Context ctx) throws Exception {
        RxRatpack.promiseSingle(myService.getValueFromDatabase())
                .then(result -> ctx.render(format("Response from database: %s", result)));
    }
}
