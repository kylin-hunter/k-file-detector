#!/usr/bin/env bash
# exit whenever encounter errors
set -e

./script/build-script/build-before.sh

#gradle clean build kylinTaskDockerImageBuild -x test -Pprofile=release

./gradlew clean build  -x test -Pprofile=release

./script/build-script/build-after.sh
