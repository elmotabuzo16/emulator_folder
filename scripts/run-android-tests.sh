: ${DEVICE?"envvar not set: DEVICE"}
: ${ANDROID_HOME?"envvar not set: ANDROID_HOME"}
: ${FLAVOR:=VitalityActiveDebug}
: ${TEST_TAGS?"envvar not set: TEST_TAGS"}

STOP_EMULATOR=scripts/stop-emulator.sh
TAGS="$TEST_TAGS$bamboo_TEST_TAGS_SUFFIX"

[ -f $STOP_EMULATOR ] || (echo "stop-emulator not found $STOP_EMULATOR"; exit 1)

echo "Building for deployment on emulator '$DEVICE', from sdk dir $ANDROID_HOME, flavor $FLAVOR, running tests with tags $TAGS"

echo "stopping previous emulators"
sh -e $STOP_EMULATOR
echo ""

sh -e scripts/start-emulator.sh

echo "==== starting tests ===="
chmod +x gradlew
echo "starting tests with tags $TAGS"
set +e
./gradlew connected$FLAVOR -P gherkinTags="$TAGS"
PASSED=$?
echo ""

echo "shutting down emulator"
sh -e $STOP_EMULATOR

if [ $PASSED -eq 0 ] ; then
    echo "==== DONE! ===="
else
    echo "==== FAILED! ===="
    exit 1
fi
