#!/bin/bash

echo "Enter Your Stack Name:"
read stackname

instanceName="$stackname-csye6225-instance"

aws cloudformation create-stack --template-body file://./csye6225-cf-application.json --stack-name ${stackname} --parameters ParameterKey=InstanceName,ParameterValue=$instanceName
