#Instructions to Run the Script

"csye6225-aws-cf-create-stack.sh" Script will

    Create a cloudformation stack taking STACK_NAME as parameter
    Create and configure required networking resources
    Create a Virtual Private Cloud (VPC) resource called STACK_NAME-csye6225-vpc
    Create Internet Gateway resource called STACK_NAME-csye6225-InternetGateway
    Attach the Internet Gateway to STACK_NAME-csye6225-vpc VPC
    Create a public Route Table called STACK_NAME-csye6225-public-route-table
    Create a public route in STACK_NAME-csye6225-public-route-table route table with destination CIDR block 0.0.0.0/0 and STACK_NAME-csye6225-InternetGateway as the target

"csye6225-aws-cf-terminate-stack.sh" Script will

    Delete the stack and all networking resources. Script should take STACK_NAME as parameter

"csye6225-cf-networking.json"

    The cloudFormation template for this stack


Very Important: In assignment9, when switch to another region(like us-east-2), please remember these things:
1. create a lambda function according to lambda funciton name in the json template.
2. certificate the domain in certificate manager, and update it in the application json template.
Notice! the us-east-1 ACM arn is
	arn:aws:acm:us-east-1:169212139838:certificate/15e1fae8-ae59-48bb-8c4b-6d12e1baed33
us-east-2 ACM arn is
	arn:aws:acm:us-east-2:169212139838:certificate/fc8d6767-173c-44aa-b9bb-f34e65f5ce5b

3. image id: 
	us-east-1: ami-66506c1c
	east-2: ami-916f59f4(should be changed in the application shell script.)

4. in Application shell script, Param: KeyPairName
    east-1: xyzdemo
    east-2: region2Key


