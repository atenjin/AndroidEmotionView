#!/bin/sh

if [[ $# -gt 1 ]]
then
	echo "release.sh <title>"
	exit 1
fi

export ANDROID_HOME=/Users/zengjin/sdk

cd app
gradle clean assembleRelease
cd build/outputs/apk
apktool decode -o app-release-unaligned.apk

titles=(`echo "$1" | tr "," "\n"`)

for title in "${titles[@]}"
do
	sed 's/android:label="[^"]*"/android:label="'"$title"'"/g' app/AndroidManifest.xml > app/AndroidManifest.xml.new
	mv app/AndroidManifest.xml.new app/AndroidManifest.xml
	apktool build -o app_${title}.apk app
done