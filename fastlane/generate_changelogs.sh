#!/bin/bash

set +x

cat FULL_CHANGELOG.md | grep -Ev '.+\[Internal\].+|.+\[Fastlane\].+' > CA_CHANGELOG.md
cat FULL_CHANGELOG.md | grep -Ev '.+\[Internal\].+|.+\[Fastlane\].+' | grep -E ".+\[CA\].+|Sumitomo|SLI" > SLI_CHANGELOG.md
cat FULL_CHANGELOG.md | grep -Ev '.+\[Internal\].+|.+\[Fastlane\].+' | grep -E ".+\[CA\].+|UKEssentials|UKE" > UKE_CHANGELOG.md
cat FULL_CHANGELOG.md | grep -Ev '.+\[Internal\].+|.+\[Fastlane\].+' | grep -E ".+\[CA\].+|IGI|IGIVitality" > IGI_CHANGELOG.md

for cl in $(ls | grep CHANGELOG); do
	FLAVOR=$(echo $cl | awk -F_ '{print $1}')
	if [[ -z $(cat $cl) ]]; then
		echo "There are no changes related to [CA] or [$FLAVOR] from the last successful build." > $cl
	fi
done