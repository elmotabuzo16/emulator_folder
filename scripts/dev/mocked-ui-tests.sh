set -e

adb logcat -c
adb logcat '*:S' 'TestRunner:V' & LOGCAT_PID=$!
./gradlew connectedDevDebugAndroidTest -PandroidTestsFilter=UITestWithMockedNetwork

kill $LOGCAT_PID
