aws CloudFormation


For Assignment6&7
Steps as follows to verify if IAM role is configured correctly and then RDS is accessed by EC2 correctly.
1. Install mysql client on EC2
sudo apt-get install mysql-client

2. connect to the endpoint of the RDS MySQL
mysql -h EndPointOfYourRDSMySQL -u **** -p

3. Input password of your MySQL User

4. MySQL Query Commands:
   - use csye6225;
   - SELECT * FROM csye6225.userinfo;
   - SELECT * FROM csye6225.picture;
   - SELECT * FROM csye6225.description;
