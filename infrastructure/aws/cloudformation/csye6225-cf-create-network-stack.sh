
set -e
#Author: Junyuan Gu
echo "Author: Junyuan Gu"
echo "        gu.ju@husky.neu.edu"
#Usage: setting up Public and Private Route Table as well as Security Group using AWS Cloud Formation

echo "Enter Your Stack Name:"
read STACK_NAME

#Assign STACK_NAME to parameters
InstanceName="$STACK_NAME-csye6225-vpc"

RouteTableName="$STACK_NAME-csye6225-public-route-table"

PrivateRouteTableName="$STACK_NAME-csye6225-private-route-table"

#Create Stack:
aws cloudformation create-stack --stack-name $STACK_NAME --template-body file://csye6225-cf-networking.json --parameters ParameterKey=InstanceName,ParameterValue=$InstanceName ParameterKey=routeTableName,ParameterValue=$RouteTableName ParameterKey=privaterouteTableName,ParameterValue=$PrivateRouteTableName

#Check Stack Status
STACK_STATUS=`aws cloudformation describe-stacks --stack-name $STACK_NAME --query "Stacks[][ [StackStatus ] ][]" --output text`

#Wait until stack completely created
echo "Please wait..."

while [ $STACK_STATUS != "CREATE_COMPLETE" ]
do
	STACK_STATUS=`aws cloudformation describe-stacks --stack-name $STACK_NAME --query "Stacks[][ [StackStatus ] ][]" --output text`
done

#Find vpc Id
vpcId=`aws ec2 describe-vpcs --filter "Name=tag:Name,Values=${STACK_NAME}" --query 'Vpcs[*].{id:VpcId}' --output text`
#Rename vpc
aws ec2 create-tags --resources $vpcId --tags Key=Name,Value=$STACK_NAME-csye6225-vpc

#Find Internet Gateway
gatewayId=`aws ec2 describe-internet-gateways --filter "Name=tag:Name,Values=${STACK_NAME}" --query 'InternetGateways[*].{id:InternetGatewayId}' --output text`
#Rename Internet Gateway
aws ec2 create-tags --resources $gatewayId --tags Key=Name,Value=$STACK_NAME-csye6225-InternetGateway


#Job Done!
echo "Job Done!"
