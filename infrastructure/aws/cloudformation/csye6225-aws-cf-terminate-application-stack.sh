#!/bin/bash

echo "Enter Your Stack Name:"
read stackname

aws cloudformation delete-stack --stack-name ${stackname}&&
echo done