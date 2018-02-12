: ${bamboo_ANDROID_EMULATOR?"emulator is not set"}
: ${bamboo_ANDROID_DIR_SDK?"sdk dir is not set"}
: ${1?"no test tags specified (first argument)"}

: ${DEVICE:=$bamboo_ANDROID_EMULATOR}
: ${ANDROID_HOME:=$bamboo_ANDROID_DIR_SDK}
: ${TEST_TAGS:=$1}

echo "exporting ANDROID_HOME=$ANDROID_HOME, DEVICE=$DEVICE, TEST_TAGS=$TEST_TAGS"
export ANDROID_HOME DEVICE TEST_TAGS
sh scripts/run-android-tests.sh
