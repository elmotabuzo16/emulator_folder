: ${FLAVOR:="VitalityActive"}
: ${PACKAGE:="com.vitalityactive.va"}
if [ -z $DEVICE ] ; then
    echo "defaulting DEVICE to first emulator"
    DEVICE=`emulator -list-avds | head -n 1`
    echo "  selected $DEVICE"
fi

export FLAVOR PACKAGE DEVICE
echo "removing previous apks"
rm -vf ./app-*.apk

echo "running bamboo test script"
scripts/bamboo-run-tests.sh $1
