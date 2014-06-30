#!/bin/bash
###############################################
# Run intensive benchmark tests from here #
###############################################

declare -i lines=10;
declare -i thresholds=0;
threshold=0.0;
threshholds=0;
testData="./labels_en.nt"
testDataUrl="http://downloads.dbpedia.org/3.9/en/labels_en.nt.bz2"

./gradlew downloadTestData -P testData=${testData} -P testDataUrl=${testDataUrl};

while [ $lines -le 1000 ]
do
    while [ $thresholds -le 10 ]
    do
        ./gradlew clean;
        ./gradlew test -P threshold=${threshold} -P lines=${lines} -P testData=${testData}
        threshold=`echo "$threshold + 0.1" | bc`
        let "thresholds+=1";
    done
    lines=`echo "$lines * 10" | bc`
    threshold=0;
    thresholds=0;
done