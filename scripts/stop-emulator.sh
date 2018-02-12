if [ -z $ANDROID_HOME ] ; then
    : ${bamboo_ANDROID_DIR_SDK?"ANDROID_HOME and bamboo_ANDROID_DIR_SDK not set"}
    : ${ANDROID_HOME:=$bamboo_ANDROID_DIR_SDK}
fi
: ${ANDROID_HOME?"envvar not set: ANDROID_HOME"}

ADB=$ANDROID_HOME/platform-tools/adb

echo "==== stopping emulator ===="

# from https://stackoverflow.com/a/38652520/1016377
$ADB devices
$ADB devices | grep emulator | cut -f1 | while read line; do $ADB -s $line emu kill; done
$ADB "kill-server"
echo "emulator devices stopped"
