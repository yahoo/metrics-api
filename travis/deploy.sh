#!/usr/bin/env bash

set -e

echo "TRAVIS_TAG is ${TRAVIS_TAG}" 

echo ${TRAVIS_TAG} | grep -q "metrics_api-";
IS_NOT_RELEASE=$?

if ((${IS_NOT_RELEASE} == 1)); then
    echo "Not a tagged release version."
    echo "To release this tagged artifact into bintray, the git tag must be of"
    echo "the format: metrics_api-*.  Example:  metrics_api-1.0.1, metrics_api-1.0.2"
    echo "etc."
    exit 0
fi

test "${TRAVIS_PULL_REQUEST}" == "false"
test "${TRAVIS_BRANCH}" == "master"

if [ -z "${TRAVIS_TAG}" ]
then
    echo "TRAVIS_TAG tag is not set, skip deploying"
else
    echo "Publishing to bintray at https://bintray.com/yahoo/maven/metrics_api"
    mvn deploy --settings travis/settings.xml
fi
