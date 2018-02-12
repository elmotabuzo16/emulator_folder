: ${FLAVOR:=$bamboo_TARGET_NAME}
: ${ANDROID_HOME:=$bamboo_ANDROID_DIR_SDK}

[ -z $FLAVOR ] && echo "FLAVOR not set" && exit 1
[ -z $ANDROID_HOME ] && echo "ANDROID_HOME not set" && exit 1

echo "\n\nRunning unit tests for FLAVOR=$FLAVOR\n\n";

UNIT="test"$FLAVOR"DebugUnitTest"
DIR="app/build/test-results/$UNIT/"
rm -rf $DIR

export ANDROID_HOME
chmod +x gradlew
if ! ./gradlew $UNIT ; then
    echo "\n\nUNIT TESTS FAILED"
    echo "xml are in $DIR\n\n"
    exit 1
fi

echo "unit test passed! xml are in $DIR\n\n"
