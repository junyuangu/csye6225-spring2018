package neu.csye6225.service;

import java.io.File;
import java.io.IOException;

/**
 * Add for Assignment 6&7
 * @author  Junyuan Gu
 * @NUID    001825583
 */
public interface IAWSUploadS3Service {
    void uploadObjectSingleOp( String keyName, String uploadFileName ) throws IOException;
    void createFolder( String folderName );
    void deleteFolder( String folderName );
}
