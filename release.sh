#!/bin/sh

if [[ $# -gt 4 || $# -lt 1 ]]
then
	echo "Missing parameters!"
	echo "release.sh <title> <keystore> <password> <password>"
	exit 1
fi

keystore=$2

cd ~/Desktop
if [ ! -f "$keystore" ]; then
	echo "keystore file not exist!"	
	cd -
	exit 1
fi
cd -

gradle clean assembleRelease

echo "gradle assemble end!"

cd app/build/outputs/apk

echo "begin to use apktool decode the apk"

apktool decode -o decode app-release-unsigned.apk

titles=(`echo "$1" | tr "," "\n"`)

password1=$3
password2=$4

for title in "${titles[@]}"
do
	sed 's/android:label="[^"]*"/android:label="'"$title"'"/g' decode/AndroidManifest.xml > decode/AndroidManifest.xml.new
	mv decode/AndroidManifest.xml.new decode/AndroidManifest.xml
	apktool build -o app_${title}.apk decode
	jarsigner -verbose -sigalg MD5withRSA -digestalg SHA1 -keystore ~/Desktop/$keystore -storepass $password1 -keypass $password2 app_${title}.apk $keystore
	jarsigner -verify app_${title}.apk
	zipalign -v 4 app_${title}.apk app_${title}-signed.apk
	rm app_${title}.apk
done


rm -r decode
rm app-release-unsigned.apk