This is an example Ratpack app that:

1. Is implemented in Java
2. Use Guice for dependency injection
3. Uses the `ratpack` Gradle plugin
4. Exposes several endpoints to exercise behaviours with different downstream systems (http client, cassandra) in a non-blocking manner
    - http://localhost:5050/injected: no ratpack promise, the response returned contains a value retrieve from a synchronous service call
    - http://localhost:5050/observable: observable with ratpack promise, the response returned contains a value retrieve from a synchronous service call
    - http://localhost:5050/httpclient: the response contains a value retrieved from an arbitrary downstream http system (e.g. google) called aynschronoulsy
    - http://localhost:5050/hystrixhttpclient: same as previous point but the service call is wrapped in an hystrix command
    - http://localhost:5050/cassandra: the response contains a value retrieved from a datasource (cassandra) asynchronously

## Getting Started

Check this project out and run `./runApp.sh` or `./runApp.bat`
Your app should now be running on http://localhost:5050


## Generate traffic
Run script generateTraffic.sh to make calls to the various endpoints


## More on Ratpack

To learn more about Ratpack, visit http://www.ratpack.io
