import com.amazonaws.services.elasticbeanstalk.AWSElasticBeanstalk;
import com.amazonaws.services.elasticbeanstalk.AWSElasticBeanstalkClientBuilder;
import com.amazonaws.services.elasticbeanstalk.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class BeanstalkService {
    private AWSElasticBeanstalk _beanstalk;
    private String _appName;

    public BeanstalkService(String applicationName) {
        _appName = applicationName;
        _beanstalk = AWSElasticBeanstalkClientBuilder.standard().build();
    }

    public void createApplication() {
        CreateApplicationRequest request = new CreateApplicationRequest();
        request.setApplicationName(_appName);
        request.setDescription(String.format("created %s %s using Java SDK", java.time.LocalDate.now(), java.time.LocalTime.now()));
        CreateApplicationResult result = _beanstalk.createApplication(request);
    }

    public void createApplicationVersion() {
        //upload the moqui code zip file to S3
        S3Service _s3Service = new S3Service();
        String bucketName = _s3Service.bucketName;
        String keyName = _s3Service.uploadSourceFile();

        S3Location s3Location = new S3Location();
        s3Location.setS3Bucket(bucketName);
        s3Location.setS3Key(keyName);

        CreateApplicationVersionRequest request = new CreateApplicationVersionRequest();
        request.setApplicationName(_appName);
        request.setDescription(String.format("created %s %s using Java SDK", java.time.LocalDate.now(), java.time.LocalTime.now()));
        request.setAutoCreateApplication(true);
        request.setProcess(false);
        request.setVersionLabel("v-" + _appName);
        request.setSourceBundle(s3Location);

        _beanstalk.createApplicationVersion(request);
    }

    public void createEnvironment() {
        //create Tags list for environment request
        Tag tag = new Tag();
        tag.setKey("EnvTag");
        tag.setValue(_appName);
        List<Tag> tagList = new ArrayList<Tag>();
        tagList.add(tag);

        //create EnvironmentTier list for environment request
        EnvironmentTier environmentTier = new EnvironmentTier();
        environmentTier.setName("WebServer");
        environmentTier.setType("Standard");
        environmentTier.setVersion("1.0");

        //create OptionsSettings list for environment request
        List<ConfigurationOptionSetting> optionSettings = new ArrayList<ConfigurationOptionSetting>();
        ConfigurationOptionSetting setting1 = new ConfigurationOptionSetting();
        setting1.setNamespace("aws:elasticbeanstalk:application:environment");
        setting1.setOptionName("instance_purpose");
        setting1.setValue("dev");
        optionSettings.add(setting1);
        ConfigurationOptionSetting setting2 = new ConfigurationOptionSetting();
        setting2.setNamespace("aws:elasticbeanstalk:application:environment");
        setting2.setOptionName("default_locale");
        setting2.setValue("en_US");
        optionSettings.add(setting2);
        ConfigurationOptionSetting setting3 = new ConfigurationOptionSetting();
        setting3.setNamespace("aws:elasticbeanstalk:application:environment");
        setting3.setOptionName("default_time_zone");
        setting3.setValue("US/Pacific");
        optionSettings.add(setting3);
        ConfigurationOptionSetting setting4 = new ConfigurationOptionSetting();
        setting4.setNamespace("aws:elasticbeanstalk:application:environment");
        setting4.setOptionName("database_time_zone");
        setting4.setValue("US/Pacific");
        optionSettings.add(setting4);
        ConfigurationOptionSetting setting5 = new ConfigurationOptionSetting();
        setting5.setNamespace("aws:autoscaling:launchconfiguration");
        setting5.setOptionName("InstanceType");
        setting5.setValue("t2.small");
        optionSettings.add(setting5);

        CreateEnvironmentRequest request = new CreateEnvironmentRequest();
        request.setCNAMEPrefix(getAvailablePrefix());
        request.setApplicationName(_appName);
        request.setDescription(String.format("created %s %s using Java SDK", java.time.LocalDate.now(), java.time.LocalTime.now()));
        request.setEnvironmentName(String.format("DEV-%s", _appName));
        request.setSolutionStackName("64bit Amazon Linux 2018.03 v2.7.2 running Java 8");
        request.setTags(tagList);
        request.setTier(environmentTier);
        request.setOptionSettings(optionSettings);
        CreateEnvironmentResult createEnvironment = _beanstalk.createEnvironment(request);

        System.out.println(String.format("Waiting for environment creation to complete %s...", java.time.LocalTime.now()));
        if (isEnvironmentCreated()) {
            return;
        }
    }

    public void deployApplication() {
        //deploy application code
        UpdateEnvironmentRequest request = new UpdateEnvironmentRequest();
        request.setEnvironmentName(String.format("DEV-%s", _appName));
        request.setVersionLabel(String.format("v-%s", _appName));
        _beanstalk.updateEnvironment(request);
    }

    private String getAvailablePrefix() {
        String prefix = _appName;
        Random rand = new Random();

        CheckDNSAvailabilityRequest request = new CheckDNSAvailabilityRequest();
        request.setCNAMEPrefix(prefix);

        while (true) {
            CheckDNSAvailabilityResult response = _beanstalk.checkDNSAvailability(request);
            if (response.getAvailable()) {
                return prefix;
            }
            //try new prefix
            int randNum = rand.nextInt(999) + 1;
            prefix = _appName + randNum;
            request.setCNAMEPrefix(prefix);
        }
    }

    private boolean isEnvironmentCreated() {
        while (!testEnvStatus()) {
            System.out.print(".");
            try
            {
                TimeUnit.SECONDS.sleep(5);
            }
            catch(InterruptedException ex) { }
        }

        System.out.println(".");
        System.out.println(String.format("Environment created: %s.", java.time.LocalTime.now()));
        return true;
    }

    private boolean testEnvStatus() {
        DescribeEnvironmentsRequest request = new DescribeEnvironmentsRequest();
        request.setApplicationName(_appName);
        DescribeEnvironmentsResult resp = _beanstalk.describeEnvironments(request);
        String status = resp.getEnvironments().get(0).getStatus();
        return status.toLowerCase().equals("ready");
    }
}
