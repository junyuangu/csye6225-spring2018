#!/bin/bash

echo "Enter Your Stack Name:"
read stackname

# Update stack to disable ec2 termination protection
# echo "Updating CloudFormation Stack to Disable EC2 termination protection"
# aws cloudformation update-stack --stack-name $stackname --template-body file://./updateStack.json
# sleep 60s

#Query the stack
aws cloudformation describe-stacks --stack-name $stackname

#Delete the cloudformation stack
aws cloudformation delete-stack --stack-name ${stackname}&&

echo done
