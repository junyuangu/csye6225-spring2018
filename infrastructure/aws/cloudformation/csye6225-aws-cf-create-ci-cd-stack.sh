#!/bin/bash

#Author: Junyuan Gu
echo "Author: Junyuan Gu"
echo "Email:  gu.ju@husky.neu.edu"

echo "Enter Your Stack Name:"
read stackname

STACK_CF=""
STACK_STATUS=""

#read VPCTagValue
VPCTagValue="assign9Network-csye6225-vpc"
VPCId=`aws ec2 describe-vpcs --filter "Name=tag:Name,Values=$VPCTagValue" --query 'Vpcs[*].{id:VpcId}' --output text`

EC2_Subnet_ID=$(aws ec2 describe-subnets --filters Name=tag:aws:cloudformation:logical-id,Values=SubnetForWebServers | jq -r '.Subnets[0].SubnetId')
EC2_Subnet2_ID=$(aws ec2 describe-subnets --filters Name=tag:aws:cloudformation:logical-id,Values=SubnetForAutoScaling | jq -r '.Subnets[0].SubnetId')

EC2_SecurityGroup_ID=$(aws ec2 describe-security-groups --filters Name=tag:aws:cloudformation:logical-id,Values=WebServerSecurityGroup | jq -r '.SecurityGroups[0].GroupId')
RDS_SecurityGroup_ID=$(aws ec2 describe-security-groups --filters Name=tag:aws:cloudformation:logical-id,Values=DBServerSecurityGroup | jq -r '.SecurityGroups[0].GroupId')

echo "EC2_Subnet_ID: $EC2_Subnet_ID"
echo "EC2_Subnet2_ID: $EC2_Subnet2_ID"
echo "EC2_SecurityGroup_ID: $EC2_SecurityGroup_ID"
echo "RDS_SecurityGroup_ID: $RDS_SecurityGroup_ID"

ParamECTWOSGID=$EC2_SecurityGroup_ID
ParamRDSSGID=$RDS_SecurityGroup_ID

#Create Stack:
STACK_CF=$(aws cloudformation create-stack --template-body file://csye6225-cf-ci-cd.json --stack-name $stackname --capabilities CAPABILITY_NAMED_IAM --parameters ParameterKey=CodeDeployAppName,ParameterValue=MyApplication ParameterKey=TagKey,ParameterValue=name ParameterKey=TagValue,ParameterValue=csye6225-MyAutoScalingGroup ParameterKey=EC2SubnetId,ParameterValue=$EC2_Subnet_ID ParameterKey=EC2Subnet2Id,ParameterValue=$EC2_Subnet2_ID ParameterKey=vpcId,ParameterValue=$VPCId ParameterKey=WebServerSecurityGroupId,ParameterValue=$ParamECTWOSGID  ParameterKey=ImageIdOfEC2Instance,ParameterValue=ami-66506c1c ParameterKey=ExistingKeyPairName,ParameterValue=xyzdemo ParameterKey=RDSSecurityGroupId,ParameterValue=$ParamRDSSGID)

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
