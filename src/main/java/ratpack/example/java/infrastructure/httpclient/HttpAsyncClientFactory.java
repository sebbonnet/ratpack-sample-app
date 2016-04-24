package ratpack.example.java.infrastructure.httpclient;

import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.nio.client.HttpAsyncClient;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.apache.http.nio.reactor.IOReactorException;

public class HttpAsyncClientFactory {

    public static HttpAsyncClient createHttpClient() {
        try {
            ConnectingIOReactor ioReactor = new DefaultConnectingIOReactor(IOReactorConfig.custom()
                    .setConnectTimeout(1000)
                    .setSelectInterval(50)
                    .setSoTimeout(2000)
                    .build());
            PoolingNHttpClientConnectionManager cm = new PoolingNHttpClientConnectionManager(ioReactor);
            cm.setMaxTotal(10);
            cm.setDefaultMaxPerRoute(10);

            CloseableHttpAsyncClient httpclient = HttpAsyncClients.custom()
                    .setConnectionManager(cm)
                    .build();
            httpclient.start();
            return httpclient;
        } catch (IOReactorException e) {
            throw new RuntimeException("Failed to create HttpClient", e);
        }
    }
}
