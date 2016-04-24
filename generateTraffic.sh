#!/usr/bin/env bash

COUNT=${1:-1}

for (( i=0; i < COUNT; ++i ))
do
    curl http://localhost:5050/injected && echo ""
    curl http://localhost:5050/observable && echo ""
    curl http://localhost:5050/httpclient && echo ""
    curl http://localhost:5050/hystrixhttpclient && echo ""
    curl http://localhost:5050/cassandra && echo ""
done