: ${FLAVOR:=$bamboo_TARGET_NAME}
: ${ANDROID_HOME:=$bamboo_ANDROID_DIR_SDK}

[ -z $FLAVOR ] && echo "FLAVOR not set" && exit 1
[ -z $ANDROID_HOME ] && echo "ANDROID_HOME not set" && exit 1

echo "\n\nBuilding FLAVOR=$FLAVOR\n\n";

APP="assemble"$FLAVOR
TEST="assemble"$FLAVOR"DebugAndroidTest"
if [ "$DEBUG_ONLY" != "" ] ; then
    APP=$APP"Debug"
    echo "building debug version only (DEBUG_ONLY is set)"
fi

export ANDROID_HOME
chmod +x gradlew
if ! ./gradlew $APP $TEST ; then
    echo "\n\nBUILD FAILED"
    exit 1
fi
echo "built $FLAVOR app ($APP) and test ($TEST) apks"

zip -j apks.zip app/build/outputs/apk/*.apk
