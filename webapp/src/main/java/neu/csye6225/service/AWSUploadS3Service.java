package neu.csye6225.service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Profile("test")
@EnableWebMvc
@Service
public class AWSUploadS3Service implements IAWSUploadS3Service{
    private Logger logger = LoggerFactory.getLogger(AWSUploadS3Service.class);
    private static final String SUFFIX = "/";

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Autowired
    private AmazonS3 s3client;

    @Override
    public void uploadObjectSingleOp( String keyName, String uploadFileName ) throws IOException {
        try {
            logger.info("Uploading a new object to S3 from a file.");
            System.out.println( "\nbucketName: " + bucketName + "\nkeyName: " + keyName );
            File uploadFile = new File( uploadFileName );
            s3client.putObject( new PutObjectRequest(
                    bucketName, keyName, uploadFile ) );

        } catch (AmazonServiceException ase) {
            logger.info("Caught an AmazonServiceException, which " +
                    "means your request made it " +
                    "to Amazon S3, but was rejected with an error response" +
                    " for some reason.");
            logger.info("Error Message:    " + ase.getMessage());
            logger.info("HTTP Status Code: " + ase.getStatusCode());
            logger.info("AWS Error Code:   " + ase.getErrorCode());
            logger.info("Error Type:       " + ase.getErrorType());
            logger.info("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            logger.info("Caught an AmazonClientException, which " +
                    "means the client encountered " +
                    "an internal error while trying to " +
                    "communicate with S3, " +
                    "such as not being able to access the network.");
            logger.info("Error Message: " + ace.getMessage());
        }
    }

    @Override
    public  void createFolder( String folderName ) {
        // create meta-data for your folder and set content-length to 0
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(0);
        // create empty content
        InputStream emptyContent = new ByteArrayInputStream(new byte[0]);
        // create a PutObjectRequest passing the folder name suffixed by /
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName,
                folderName + SUFFIX, emptyContent, metadata);
        // send request to S3 to create folder
        s3client.putObject(putObjectRequest);
    }

    /**
     * This method first deletes all the files in given folder and then the
     * folder itself
     */
    @Override
    public void deleteFolder( String folderName ) {
        List fileList =
                s3client.listObjects(bucketName, folderName).getObjectSummaries();
        for( Object file : fileList ) {
            S3ObjectSummary objectSummary = (S3ObjectSummary)file;
            s3client.deleteObject( bucketName, objectSummary.getKey() );
        }
        s3client.deleteObject(bucketName, folderName);
    }
}