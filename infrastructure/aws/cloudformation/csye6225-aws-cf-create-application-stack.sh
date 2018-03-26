#!/bin/bash

set -e
#Author: Junyuan Gu
echo "Author: Junyuan Gu"
echo "Email:  gu.ju@husky.neu.edu"

echo "Enter Your Stack Name:"
read stackname

STATUS_CF=""
STATUS_STATE=""

#instancename="$stackname-csye6225-ec2"
instancename="MyEC2ForCodeDeployInstance"

echo $instancename

EC2_Subnet_ID=$(aws ec2 describe-subnets --filters Name=tag:aws:cloudformation:logical-id,Values=SubnetForWebServers | jq -r '.Subnets[0].SubnetId')
EC2_SecurityGroup_ID=$(aws ec2 describe-security-groups --filters Name=tag:aws:cloudformation:logical-id,Values=WebServerSecurityGroup | jq -r '.SecurityGroups[0].GroupId')
RDS_SecurityGroup_ID=$(aws ec2 describe-security-groups --filters Name=tag:aws:cloudformation:logical-id,Values=DBServerSecurityGroup | jq -r '.SecurityGroups[0].GroupId')

val1="EC2_Subnet_ID: $EC2_Subnet_ID"
val2="EC2_SecurityGroup_ID: $EC2_SecurityGroup_ID"
val3="RDS_SecurityGroup_ID: $RDS_SecurityGroup_ID"

echo $val1
echo $val2
echo $val3
echo $RDS_SecurityGroup_ID

#Create Stack:
STATUS_CF=$(aws cloudformation create-stack --template-body file://csye6225-cf-application.json --capabilities CAPABILITY_NAMED_IAM --stack-name $stackname --parameters ParameterKey=EC2InstanceTagValue,ParameterValue=$instancename
Parameterkey=EC2SubnetId,ParameterValue=$EC2_Subnet_ID ParameterKey=WebServerSecurityGroupId,ParameterValue=$EC2_SecurityGroup_ID )
# Parameterkey=RDSSGId,ParameterValue=$RDS_SecurityGroup_ID
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
