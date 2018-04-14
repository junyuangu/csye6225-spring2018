#!/bin/bash

set -e
#Author: Junyuan Gu
echo "Author: Junyuan Gu"
echo "Email:  gu.ju@husky.neu.edu"

echo "Enter Your Stack Name:"
read stackname

# echo "Enter your VPC tag value when Key=Name, Value=?"
# assign9Network-csye6225-vpc
#read VPCTagValue
VPCTagValue="assign9Network-csye6225-vpc"
# Find vpc Id
VPCId=`aws ec2 describe-vpcs --filter "Name=tag:Name,Values=$VPCTagValue" --query 'Vpcs[*].{id:VpcId}' --output text`
val4="VPC ID: $VPCId"

STATUS_CF=""
STATUS_STATE=""

#instancename="$stackname-csye6225-ec2"
instancename="MyEC2ForCodeDeployInstance"

echo $intancename

EC2_Subnet_ID=$(aws ec2 describe-subnets --filters Name=tag:aws:cloudformation:logical-id,Values=SubnetForWebServers | jq -r '.Subnets[0].SubnetId')
EC2_Subnet2_ID=$(aws ec2 describe-subnets --filters Name=tag:aws:cloudformation:logical-id,Values=SubnetForAutoScaling | jq -r '.Subnets[0].SubnetId')
EC2_SecurityGroup_ID=$(aws ec2 describe-security-groups --filters Name=tag:aws:cloudformation:logical-id,Values=WebServerSecurityGroup | jq -r '.SecurityGroups[0].GroupId')
RDS_SecurityGroup_ID=$(aws ec2 describe-security-groups --filters Name=tag:aws:cloudformation:logical-id,Values=DBServerSecurityGroup | jq -r '.SecurityGroups[0].GroupId')

val1="EC2_Subnet_ID: $EC2_Subnet_ID"
valSubnet2="EC2_Subnet2_ID: $EC2_Subnet2_ID"
val2="EC2_SecurityGroup_ID: $EC2_SecurityGroup_ID"
val3="RDS_SecurityGroup_ID: $RDS_SecurityGroup_ID"

echo $val1
echo $valSubnet2
echo $val2
echo $val3
echo $val4

ParamECTWOSUBNETID=$EC2_Subnet_ID
ParamECTWOSGID=$EC2_SecurityGroup_ID
ParamRDSSGID=$RDS_SecurityGroup_ID
ParamVpcID=$VPCId

ParamLAMBDAFUNCARN=$(aws lambda get-function --function-name "lambda-LogEvent" --query "Configuration.FunctionArn" --output text)
#echo $ParamLAMBDAFUNCARN

ParamALBTargetGroupARN=$(aws autoscaling describe-load-balancer-target-groups --auto-scaling-group-name MyAutoScalingGroup | jq -r '.LoadBalancerTargetGroups[0].LoadBalanceTargetGroupArn')
echo $ParamALBTargetGroupARN

#Create Stack:
STATUS_CF=$(aws cloudformation create-stack --stack-name $stackname --template-body file://csye6225-cf-application.json --capabilities CAPABILITY_NAMED_IAM --parameters ParameterKey=ImageIdOfEC2Instance,ParameterValue=ami-66506c1c ParameterKey=TypeOfEC2Instance,ParameterValue=t2.micro ParameterKey=WebServerSecurityGroupId,ParameterValue=$ParamECTWOSGID ParameterKey=EC2SubnetId,ParameterValue=$ParamECTWOSUBNETID ParameterKey=RDSSecurityGroupId,ParameterValue=$ParamRDSSGID ParameterKey=ExistingKeyPairName,ParameterValue=region2Key ParameterKey=EC2VolumeType,ParameterValue=gp2 ParameterKey=EC2VolumeSize,ParameterValue=16 ParameterKey=TagKey,ParameterValue=Name ParameterKey=EC2InstanceTagValue,ParameterValue=$instancename ParameterKey=WebAppS3BucketName,ParameterValue=web-app.csye6225-spring2018-guju.me ParameterKey=rdsParamStorageSize,ParameterValue=10 ParameterKey=rdsParamDBName,ParameterValue=csye6225 ParameterKey=rdsParamEngine,ParameterValue=MySQL ParameterKey=LambdaFunctionARN,ParameterValue=$ParamLAMBDAFUNCARN ParameterKey=rdsParamEngineVersion,ParameterValue=5.6.37 ParameterKey=rdsParamDBInsClass,ParameterValue=db.t2.medium ParameterKey=rdsParamDBInsId,ParameterValue=csye6225-spring2018 ParameterKey=rdsParamUsername,ParameterValue=csye6225master ParameterKey=rdsParamPassword,ParameterValue=csye6225password ParameterKey=rdsParamDBTagVal,ParameterValue=csye6225MyDBInstance ParameterKey=DBSubnetGroup,ParameterValue=RDSsubnet-group ParameterKey=vpcId,ParameterValue=$ParamVpcID ParameterKey=EC2Subnet2Id,ParameterValue=$EC2_Subnet2_ID ParameterKey=ALBTargetGroupArn,ParameterValue=$ParamALBTargetGroupARN)


echo $STATUS_CF
#Wait until stack completely created
echo "Please wait..."

#Check Stack Status
STACK_STATUS=`aws cloudformation wait stack-create-complete --stack-name $stackname`

if [ ! -z "$STACK_CF" ] && [ -z "$STACK_STATUS" ]; then
  #statements
  echo "STACK_CF: $STACK_CF"
  echo "STACK_STATUS: $STACK_STATUS"
  echo "Success!"
else
  echo "STACK_CF: $STACK_CF"
  echo "STACK_STATUS: $STACK_STATUS"
  echo "Job Done!"
fi
