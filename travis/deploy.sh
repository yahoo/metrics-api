#!/usr/bin/env bash

set -e

echo "TRAVIS_TAG is ${TRAVIS_TAG}" 

echo ${TRAVIS_TAG} | grep -q "metrics_api-";
IS_NOT_RELEASE=$?

if ((${IS_NOT_RELEASE} == 1)); then
    echo "Not a tagged release version."
    echo "To release this tagged artifact into bintray, the git tag must be of"
    echo "the format: metrics_api-*.  Example:  metrics_api-1.0.1, sshd_proxy-1.0.2"
    echo "etc."
    exit 0
fi

echo "Publishing to bintray at https://bintray.com/yahoo/metrics_api"

test "${TRAVIS_PULL_REQUEST}" == "false"
test "${TRAVIS_BRANCH}" == "master"
test "${TRAVIS_TAG}" != ""
mvn deploy --settings travis/settings.xml
