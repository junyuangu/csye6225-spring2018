#!/bin/bash

#Author: Junyuan Gu
echo "Author: Junyuan Gu"
echo "Email:  gu.ju@husky.neu.edu"

echo "Enter Your Stack Name:"
read stackname

STACK_CF=""
STACK_STATUS=""

#Create Stack:
STACK_CF=$(aws cloudformation create-stack --template-body file://csye6225-cf-ci-cd.json --stack-name $stackname --capabilities CAPABILITY_NAMED_IAM --parameters ParameterKey=CodeDeployAppName,ParameterValue=MyApplication
Parameterkey=S3Key,ParameterValue=MyApplication.zip ParameterKey=TagKey,ParameterValue=name ParameterKey=TagValue,ParameterValue=csye6225-MyAutoScalingGroup)

#Wait until stack completely created
echo "Please wait..."

#Check Stack Status
STACK_STATUS=`aws cloudformation wait stack-create-complete --stack-name $stackname`

if [ ! -z "$STACK_CF" ] && [ -z "$STACK_STATUS" ]; then
  #statements
  echo "Job Done!"
else
   echo "Failure!"
fi
