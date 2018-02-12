set -e

./gradlew testDevDebugUnitTest

echo "starting tests on android emulator/device"
adb logcat -c
adb logcat '*:S' 'TestRunner:V' & LOGCAT_PID=$!

./gradlew connectedDevDebugAndroidTest -PandroidTestsFilter=UnitTestRequiringAndroidContext
./gradlew connectedDevDebugAndroidTest -PandroidTestsFilter=RepositoryTests

kill $LOGCAT_PID
