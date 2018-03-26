#!/bin/bash

echo "Enter Your Stack Name:"
read STACK_NAME

# Update stack to disable ec2 termination protection
# echo "Updating CloudFormation Stack to Disable EC2 termination protection"
# aws cloudformation update-stack --stack-name $stackname --template-body file://./updateStack.json
# sleep 60s

STATUS=2

aws cloudformation delete-stack --stack-name $STACK_NAME
aws cloudformation wait stack-delete-complete --stack-name $STACK_NAME
STATUS=$(aws cloudformation describe-stacks --stack-name $STACK_NAME)

if [ -z "$STATUS" ]; then
   echo "Stack deleted!"
 else
   echo "Stack delete failed!"
fi
