#!/bin/bash

CLASSPATH='copyit-general.jar:slf4j-api-1.7.5.jar:slf4j-jdk14-1.7.5.jar:jcl-over-slf4j-1.7.5.jar:commons-io-2.4.jar:gson-2.2.2.jar:httpcore-4.2.2.jar:httpclient-4.2.3.jar:libdbus-java-2.7.jar:swing2swt.jar:unix-0.5.jar:hexdump-0.2.jar'

MACHINE_TYPE=`uname -m`
if [ ${MACHINE_TYPE} == 'x86_64' ]; then
    CLASSPATH="$CLASSPATH:swt-4.2.1-gtk-linux-x86_64.jar"
else
    CLASSPATH="$CLASSPATH:swt-4.2.1-gtk-linux-x86.jar"
fi

export CLASSPATH

java -cp $CLASSPATH net.mms_projects.copy_it.run_configurations.Zip
