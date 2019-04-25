#!/usr/bin/env bash

set -e

echo "TRAVIS_TAG is ${TRAVIS_TAG}" 

#echo ${TRAVIS_TAG} | grep -q "metrics_api-";
echo "IS_NOT_RELEASE is ${IS_NOT_RELEASE}"
#IS_NOT_RELEASE=$?

#if ((${IS_NOT_RELEASE} == 1)); then
#    echo "Not a tagged release version."
#    echo "To release this tagged artifact into bintray, the git tag must be of"
#    echo "the format: metrics_api-*.  Example:  metrics_api-1.0.1, metrics_api-1.0.2"
#    echo "etc."
#    exit 0
#fi

echo "TRAVIS_PULL_REQUEST is ${TRAVIS_PULL_REQUEST}"
echo "TRAVIS_BRANCH is ${TRAVIS_BRANCH}"

test "${TRAVIS_PULL_REQUEST}" == "false"
test "${TRAVIS_BRANCH}" == "master"

echo "This is master branch build"

if [ -z "${TRAVIS_TAG}" ]
then
    echo "TRAVIS_TAG tag is not set, skip deploying"
else
    echo "Publishing to bintray at https://bintray.com/yahoo/maven/metrics_api"
    echo "TRAVIS_TAG tag is set, starting deploying"
    mvn deploy --settings travis/settings.xml
fi
