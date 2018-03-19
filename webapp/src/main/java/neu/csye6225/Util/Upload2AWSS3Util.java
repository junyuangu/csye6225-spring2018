package neu.csye6225.Util;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;
import java.io.File;

public class Upload2AWSS3Util {

    public void uploadSingleFile( String file_path, String bucket_name, String key_name ) {
        File f = new File(file_path);
        TransferManager xfer_mgr = new TransferManager();
        try {
            Upload xfer = xfer_mgr.upload(bucket_name, key_name, f);
            // loop with Transfer.isDone()
            //  or block with Transfer.waitForCompletion()
        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
            System.exit(1);
        }
        xfer_mgr.shutdownNow();
    }
}

