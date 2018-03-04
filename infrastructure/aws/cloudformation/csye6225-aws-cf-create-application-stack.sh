#!/bin/bash

set -e
#Author: Junyuan Gu
echo "Author: Junyuan Gu"
echo "        gu.ju@husky.neu.edu"

echo "Enter Your Stack Name:"
read stackname

instancename="$stackname-csye6225-ec2"

echo $instancename

#Create Stack:
aws cloudformation create-stack --template-body file://./csye6225-cf-application.json --stack-name ${stackname} --parameters ParameterKey=InstanceName,ParameterValue=$instancename


#Check Stack Status
STACK_STATUS=`aws cloudformation describe-stacks --stack-name $stackname --query "Stacks[][ [StackStatus ] ][]" --output text`

#Wait until stack completely created
echo "Please wait..."

while [ $STACK_STATUS != "CREATE_COMPLETE" ]
do
	STACK_STATUS=`aws cloudformation describe-stacks --stack-name $stackname --query "Stacks[][ [StackStatus ] ][]" --output text`
done


#Job Done!
echo "Job Done!"
