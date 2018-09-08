import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;

import java.io.File;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

public class S3Service {
    private AmazonS3 _s3;
    private String _filePath = "/Users/joel/Documents/"; //use leading/trailing slash
    private String _fileName = "myapp-1.0.0.zip";
    public String bucketName;

    public S3Service() {
        _s3 = AmazonS3ClientBuilder.standard().build();
        bucketName = findS3BucketName();
    }

    public String findS3BucketName() {
        List<Bucket> bucketList = _s3.listBuckets();
        for (Bucket bucket : bucketList) {
            LocalDate createDate = bucket.getCreationDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            if (createDate.equals(java.time.LocalDate.now())) {
                return bucket.getName();
            }
        }
        return null;
    }

    public String uploadSourceFile() {
        if (bucketName == null || bucketName.isEmpty()) {
            return null;
        }

        System.out.println(String.format("Uploading Code file: %s...", java.time.LocalTime.now()));
        File file = new File(String.format("%s%s", _filePath, _fileName));
        PutObjectRequest request = new PutObjectRequest(bucketName, _fileName, file);
        PutObjectResult response = _s3.putObject(request);
        System.out.println(String.format("Code file uploaded: %s", java.time.LocalTime.now()));
        return request.getKey();
    }
}
