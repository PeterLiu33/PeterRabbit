#!/bin/bash

./gradlew clean build

java -Dcom.peterliu.peterrabbit.file.suffix=zip,rar,tar,gz,json,java,html,htm,js,png,jpg,txt,css,pptx,vm,xml,jar,pdf,sh,bat,md,c,c++,jpeg,doc,docx,ppt,gif \
     -Dcom.peterliu.peterrabbit.server.file.dictionary.filter.switch=on \
     -Dcom.peterliu.peterrabbit.file.path=/ \
     -Dcom.peterliu.peterrabbit.server.port=8301 \
     -jar build/libs/PeterRabbit-1.0-SNAPSHOT.jar