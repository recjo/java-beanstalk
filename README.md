# moqui-beanstalk

Creates AWS Elastic Beanstalk application and uploads moqui-plus-runtime war/zip file<br />
<br />
EC2: "64bit Amazon Linux 2018.03 v2.7.2 running Java 8"<br />
<br />
1. Requires credentials file at: ~/.aws/credentials<br />
("credentials" is not a directory, it is a file without extension)<br />
contents of this file are 3 lines (replace "[value]" with actual key values):<br />
_[default]<br />
aws_access_key_id = [value]<br />
aws_secret_access_key = [value]_<br />

**Note: Make sure the AWS IAM user has elastic beanstalk permissions, such as: AWSElasticBeanstalkFullAccess. *Use FullAccess at your own risk. I have not tried app with lesser Beanstalk permissions*.**<br />

<details>
  <summary>Click to see how to create IAM User with Beanstalk Permissions</summary>
<br />
   - log in to AWS console and go to IAM service<br />
<br />
   - In the navigation pane on the left, click Users<br />
<br />
   - click Add User button<br />
   - Username: java<br />
   - Access Type: check box for Programmatic access<br />
   - leave other box unchecked<br />
   - click Next Permissions button<br />
<br />
   - click rectangle for Attach Existing Policies Directly<br />
   - in the search box type: AwsElasticBeanstalkFullAccess<br />
   - check the box for AwsElasticBeanstalkFullAccess when it shows up in the grid<br />
   - click Next Review button<br />
   - click Create User button<br />
<br />
   - click the Download Credentials button and save csv file to your hard drive, just for safe keeping<br />
<br />
   - in the grid, click the Show link in the Secret Access Key column<br />
   - Cmd-C to copy, paste in the credentials file, after "aws_secret_access_key="<br />
<br />
   - in the grid, in the Access Key ID column,<br />
   - Select the value and Cmd-C to copy, paste in the credentials file, after "aws_access_key_id="<br />
<br />
   - Click the Close button<br />
</details>

2. Requires config file for region at: ~/.aws/config<br />
("config" is not a directory, it is a file without extension)<br />
contents of this file are 2 lines:<br />
_[default]<br />
region = us-east-1_<br />

3. Requires the path and filename of your moqui-plus-runtime application zip file to be defined in variables _\_filePath_ and _\_fileName_ inside file: S3Service.java<br />
<br />
