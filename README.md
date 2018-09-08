# moqui-beanstalk
creates AWS Elastic Beanstalk application and uploads moqui-plus-runtime war/zip file<br />
uses:<br />
"64bit Amazon Linux 2018.03 v2.7.2 running Java 8"<br />
<br />
1] Requires credentials file (no extension) at: ~/.aws/credentials<br />
contents of this file are 3 lines:<br />
[default]<br />
aws_access_key_id=[AWS IAM user access key id]<br />
aws_secret_access_key=[AWS IAM user access key]<br />
<br />
(make sure the AWS IAM user has elastic beanstalk permissions, such as: AWSElasticBeanstalkFullAccess)<br />
<br />
2] Requires config file for region (no extension) at: ~/.aws/config<br />
contents of this file are 2 lines:<br />
[default]<br />
region = us-east-1<br />
<br />
3] requires your moqui-plus-runtime application zip file to be defined at:<br />
S3Service.java file<br />
private variables for _filePath and _fileName<br />
