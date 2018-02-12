: ${1?"no test tags specified (first argument)"}

: ${ANDROID_HOME:=$bamboo_ANDROID_DIR_SDK}
: ${DEVICE:=$bamboo_ANDROID_EMULATOR}
: ${FLAVOR:=$bamboo_TARGET_NAME}
: ${PACKAGE:=$bamboo_TARGET_BUNDLE_IDENTIFIERS}
: ${TEST_TAGS:=$1}

[ -z $ANDROID_HOME ] && echo "ANDROID_HOME not set" && exit 1
[ -z $FLAVOR ] && echo "FLAVOR not set" && exit 1
[ -z $PACKAGE ] && echo "PACKAGE not set" && exit 1
[ -z $DEVICE ] && echo "DEVICE not set" && exit 1

export ANDROID_HOME DEVICE FLAVOR PACKAGE

EC=0
$ANDROID_HOME/platform-tools/adb "kill-server"
scripts/start-emulator.sh
if ! scripts/install.sh ; then
    echo "installation failed!"
    EC=1
elif ! scripts/test.sh $TEST_TAGS ; then
    echo "tests failed!"
    EC=2
fi
scripts/stop-emulator.sh

exit $EC
