: ${FLAVOR:="VitalityActive"}
set -e

export FLAVOR
scripts/bamboo-build-apks.sh
scripts/bamboo-unit-tests.sh
