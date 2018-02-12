#!/bin/bash
# setup networking resourses including VPC/Internet Gateway
# Route Tables and Routes using AWS CLI
# @Nenghui Fang
set -e
#get current base path
basepath=$(cd `dirname $0`; pwd)
echo $basepath
cd $basepath

#Get Arugment passed from user
STACK_NAME=$1

echo "creating VPC..."
VPC_ID=`aws ec2 create-vpc --cidr-block 10.0.0.0/16 --query 'Vpc.VpcId' --output text`
aws ec2 create-tags --resources $VPC_ID --tags Key=Name,Value=$STACK_NAME-csye6225-vpc
echo "VPC:$STACK_NAME-csye6225-vpc Id:$VPC_ID created successfully"


echo "creating Internet Gateway..."
InternetGatewayId=`aws ec2 create-internet-gateway --query 'InternetGateway.InternetGatewayId' --output text`
aws ec2 create-tags --resources $InternetGatewayId --tags Key=Name,Value=$STACK_NAME-csye6225-InternetGateway
echo "InternetGateway:$STACK_NAME-csye6225-InternetGateway ID:$InternetGatewayId created successfully" 


echo "attaching InternetGateway to VPC..."
aws ec2 attach-internet-gateway --internet-gateway-id $InternetGatewayId --vpc-id $VPC_ID
echo "attaching successfully"

echo "creating Route Table..." 
RouteTableId=`aws ec2 create-route-table --vpc-id $VPC_ID --query 'RouteTable.RouteTableId' --output text`
aws ec2 create-tags --resources $RouteTableId --tags Key=Name,Value=$STACK_NAME-csye6225-public-route-table
echo "RouteTable:$STACK_NAME-csye6225-public-route-table ID:$RouteTableId created successfully"

RouteStatus=`aws ec2 create-route --route-table-id $RouteTableId --destination-cidr-block 0.0.0.0/0 --gateway-id $InternetGatewayId --query 'Return'`
echo "Route created in RouteTable $RouteTableId with a target to Gateway $InternetGatewayId status: $RouteStatus"



