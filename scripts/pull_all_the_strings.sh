PHRASEAPP_EXEC="`PATH="$PATH:." which phraseapp`"
echo "phraseapp installed as $PHRASEAPP_EXEC"

echo "main"
PHRASEAPP_CONFIG=./app/src/main/phraseapp.yml $PHRASEAPP_EXEC pull

echo "sumitomo"
PHRASEAPP_CONFIG=./app/src/sumitomo/phraseapp.yml $PHRASEAPP_EXEC pull

echo "ukessentials"
PHRASEAPP_CONFIG=./app/src/ukessentials/phraseapp.yml $PHRASEAPP_EXEC pull

echo "igi"
PHRASEAPP_CONFIG=./app/src/igi/phraseapp.yml $PHRASEAPP_EXEC pull