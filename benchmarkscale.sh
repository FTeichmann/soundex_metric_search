#!/bin/bash
###############################################
# Run intensive benchmark tests from here #
###############################################

declare -i lines=1000;
declare -i thresholds=0;
threshold=0.75;
testData="./labels_en.nt"
testDataUrl="http://downloads.dbpedia.org/3.9/en/labels_en.nt.bz2"

./gradlew downloadTestData -P testData=${testData} -P testDataUrl=${testDataUrl};

while [ $thresholds -le 1 ]
do
    while [ $lines -le 10000 ]
    do
        ./gradlew clean;
        ./gradlew test -P threshold=${threshold} -P lines=${lines} -P testData=${testData}
        lines=`echo "$lines + 1000" | bc`;

    done
    thresholds+=1;
    threshold=1;
    lines=5000;
done
