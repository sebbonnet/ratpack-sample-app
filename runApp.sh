#!/usr/bin/env bash

./gradlew installApp "$@" && build/install/ratpack-appd-integration/bin/ratpack-appd-integration