set -e

export FLAVOR=dev
export PACKAGE=com.vitalityactive.va.dev
export DEBUG_ONLY=1

scripts/bamboo-build-apks.sh
scripts/bamboo-unit-tests.sh

echo "building all variants"
if ! ./gradlew assembleDebug ; then
    echo "\n\nall variant build failed!"
    exit 1
fi

scripts/install.sh

rm -f "test-log.txt"
scripts/test-annotated.sh 'com.vitalityactive.va.testutilities.annotations.UnitTestRequiringAndroidContext'
scripts/test-annotated.sh 'com.vitalityactive.va.testutilities.annotations.FileSystemTests'
scripts/test-annotated.sh 'com.vitalityactive.va.testutilities.annotations.RepositoryTests'
scripts/test-annotated.sh 'com.vitalityactive.va.testutilities.annotations.UITestWithMockedNetwork'

if grep "FAILURES" "test-log.txt" ; then
    echo "There were test failures"
    echo "see 'test-log.txt'"
    exit 1
fi
