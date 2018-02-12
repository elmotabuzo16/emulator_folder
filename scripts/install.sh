: ${FLAVOR?"not set"}
: ${PACKAGE?"not set"}
: ${ANDROID_HOME?"not set"}
set -e

ADB=$ANDROID_HOME/platform-tools/adb

unzip -uo apks.zip

echo "copying"
$ADB push "app-$FLAVOR-debug.apk" "/data/local/tmp/$PACKAGE"
$ADB push "app-$FLAVOR-debug-androidTest.apk" "/data/local/tmp/$PACKAGE.test"

echo "uninstalling previous version"
$ADB shell pm uninstall "$PACKAGE"

echo "installing"
$ADB shell pm install "/data/local/tmp/$PACKAGE"
$ADB shell pm install -r "/data/local/tmp/$PACKAGE.test"

# -g is not recognized on build server's adb
echo "granting permissions"
$ADB shell pm grant $PACKAGE android.permission.WRITE_EXTERNAL_STORAGE
$ADB shell pm grant $PACKAGE android.permission.READ_EXTERNAL_STORAGE
