#!/usr/bin/env bash
export APPSRV_HOME=/media/sun/A4FE1C5EFE1C2AD6/SignServer/jboss
export ANT_OPTS="-Xmx512m -XX:MaxPermSize=128m"
export SIGNSERVER_HOME=/media/sun/A4FE1C5EFE1C2AD6/SignServer/signserver
export SIGNSERVER_NODEID=node1

export LD_LIBRARY_PATH=/media/sun/A4FE1C5EFE1C2AD6/SignServer/signserver/edited/lib
export CS_PATH=/media/sun/A4FE1C5EFE1C2AD6/SignServer/signserver/edited/lib
export CS_PKCS11_R2_CFG=$CS_PATH/cs_pkcs11_R2.cfg
