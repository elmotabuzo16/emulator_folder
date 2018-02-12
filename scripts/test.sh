: ${FLAVOR?"not set"}
: ${PACKAGE?"not set"}
: ${ANDROID_HOME?"not set"}
: ${1?"test tags not set (first argument)"}

ADB=$ANDROID_HOME/platform-tools/adb

TESTS="$1$bamboo_TEST_TAGS_SUFFIX"

echo "cleanup old screenshots"
$ADB shell rm -rf "/storage/emulated/0/screenshots"
$ADB shell rm -rf "/storage/sdcard/screenshots"
rm -rf screenshots/

echo "\n\nStarting tests: $TESTS\n\n"

$ADB shell am instrument -w -r \
    -e debug false \
    -e tags $TESTS \
    $PACKAGE.test/com.vitalityactive.va.MockJUnitAndCucumberRunner \
    | tee "test-log.txt"

echo "getting screenshots"
sleep 1
$ADB pull "/storage/emulated/0/screenshots" \
    || $ADB pull "/storage/sdcard/screenshots" \
    || echo "failed to copy screenshots from adb"
zip -j screenshots.zip screenshots/*.* || echo "no screenshots"
