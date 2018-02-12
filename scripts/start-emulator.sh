: ${ANDROID_HOME?"envvar not set"}
: ${DEVICE?"envvar not set"}

ADB=$ANDROID_HOME/platform-tools/adb
EMULATOR=$ANDROID_HOME/tools/emulator

echo "==== starting emulator ===="

$ADB "start-server"
echo ""

echo "starting $DEVICE using emulator command (background, with flags: $EMULATOR_FLAGS)"
$EMULATOR -avd $DEVICE $EMULATOR_FLAGS &
sleep 5
echo ""

echo "waiting for the emulator to be ready..."
while ! $ADB shell getprop sys.boot_completed ;
do
    sleep 1
done

echo "waiting for the emulator to boot up..."
while [ "`$ADB shell getprop sys.boot_completed | tr -d '\r' `" != "1" ] ;
do
    echo "still booting up"
    sleep 1
done
echo "emulator is booted"
echo ""
