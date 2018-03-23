#!/bin/bash

set -e
#Author: Junyuan Gu
echo "Author: Junyuan Gu"
echo "Email:  gu.ju@husky.neu.edu"

echo "Enter Your Stack Name:"
read stackname

#instancename="$stackname-csye6225-ec2"
instancename="MyEC2ForCodeDeployInstance"

echo $instancename

EC2_Subnet_ID=$(aws ec2 describe-subnets --filters Name=tag:aws:cloudformation:logical-id,Values=SubnetForWebServers | jq -r '.Subnets[0].SubnetId')
EC2_SecurityGroup_ID=$(aws ec2 describe-security-groups --filters Name=tag:aws:cloudformation:logical-id,Values=WebServerSecurityGroup | jq -r '.SecurityGroups[0].GroupId')
RDS_SecurityGroup_ID=$(aws ec2 describe-security-groups --filters Name=tag:aws:cloudformation:logical-id,Values=DBServerSecurityGroup | jq -r '.SecurityGroups[0].GroupId')



#Create Stack:
aws cloudformation create-stack --template-body file://./csye6225-cf-application.json --stack-name ${stackname} --parameters ParameterKey=EC2InstanceTagValue,ParameterValue=$instancename
Parameterkey=EC2SubnetId,ParameterValue=EC2_Subnet_ID ParameterKey=WebServerSecurityGroup_Id,ParameterValue=EC2_SecurityGroup_ID Parameterkey=RDSSecurityGroup_ID,ParameterValue=RDS_SecurityGroup_ID


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
