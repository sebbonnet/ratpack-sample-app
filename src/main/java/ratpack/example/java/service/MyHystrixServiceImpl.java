package ratpack.example.java.service;

import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixObservableCommand;
import rx.Observable;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MyHystrixServiceImpl implements MyHystrixService {

    private final MyService myService;

    @Inject
    public MyHystrixServiceImpl(MyService myService) {
        this.myService = myService;
    }

    @Override
    public Observable<String> getValueFromHttpClient() {
        HystrixObservableCommand<String> hystrixCommand = new HystrixObservableCommand<String>(httpCommandSetter()) {
            @Override
            protected Observable<String> construct() {
                return myService.getValueFromHttpClient();
            }

            @Override
            protected Observable<String> resumeWithFallback() {
                return Observable.just("No response received from http client");
            }
        };

        return hystrixCommand.toObservable();
    }

    private HystrixObservableCommand.Setter httpCommandSetter() {
        return HystrixObservableCommand.Setter
                .withGroupKey(HystrixCommandGroupKey.Factory.asKey("mygroup"))
                .andCommandKey(HystrixCommandKey.Factory.asKey("MyHttpGetCommand"))
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                        .withExecutionTimeoutInMilliseconds(6000));
    }
}
