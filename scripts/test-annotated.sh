: ${PACKAGE:="com.vitalityactive.va"}
: ${1?"annotation not set (first argument)"}

echo "starting tests: $1"
adb shell am instrument -w -r \
    -e debug false \
    -e annotation $1 \
    $PACKAGE.test/com.vitalityactive.va.MockJUnitRunner \
    | tee -a "test-log.txt"
