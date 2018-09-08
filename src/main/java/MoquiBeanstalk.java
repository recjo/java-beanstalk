


/*


1] Requires credentials file (no extension) at: ~/.aws/credentials
contents of this file are 3 lines:
[default]
aws_access_key_id=[AWS IAM user access key id]
aws_secret_access_key=[AWS IAM user access key]

(make sure the AWS IAM user has elastic beanstalk permissions, such as: AWSElasticBeanstalkFullAccess)

2] Requires config file for region (no extension) at: ~/.aws/config
contents of this file are 2 lines:
[default]
region = us-east-1

3] requires your moqui-plus-runtime application zip file to be defined at:
S3Service.java file
private variables for _filePath and _fileName

 */



public class MoquiBeanstalk {

    public static void main(String[] args) {
        //to run multiple times with a different application name, uncomment below
        //Random rand = new Random();
        //int randNum = rand.nextInt(999) + 1;
        //String appName = String.format("moqui-%s", randNum);
        String appName = "moqui";

        //create beanstalk instance
        BeanstalkService _beanstalkService = new BeanstalkService(appName);

        System.out.println("Creating Beanstalk application...");
        _beanstalkService.createApplication();

        System.out.println("Creating Beanstalk application version...");
        _beanstalkService.createApplicationVersion();

        System.out.println("Creating Beanstalk environment...");
        _beanstalkService.createEnvironment();

        System.out.println("Deploying Elastic Beanstalk application...");
        _beanstalkService.deployApplication();

        System.out.println("Done. Elastic Beanstalk application now restarting.");
    }
}
