package com.apply.aws;

/**
 * Created by kunal on 5/15/17.
 */


        import com.amazonaws.services.s3.AmazonS3Client;
        import com.amazonaws.services.s3.model.*;
        import org.apache.commons.io.IOUtils;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.beans.factory.annotation.Value;
        import org.springframework.http.HttpHeaders;
        import org.springframework.http.HttpStatus;
        import org.springframework.http.MediaType;
        import org.springframework.http.ResponseEntity;
        import org.springframework.stereotype.Service;
        import org.springframework.util.StringUtils;
        import org.springframework.web.multipart.MultipartFile;
        import com.amazonaws.AmazonClientException;
        import com.amazonaws.AmazonServiceException;

        import java.io.FileInputStream;
        import java.io.FileNotFoundException;
        import java.io.IOException;
        import java.io.InputStream;
        import java.net.URLEncoder;
        import java.util.ArrayList;
        import java.util.Arrays;
        import java.util.List;

@Service
public class S3Wrapper {

    @Autowired
    private AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private PutObjectResult upload(String filePath, String uploadKey) throws FileNotFoundException {
        return upload(new FileInputStream(filePath), uploadKey);
    }

    private PutObjectResult upload(InputStream inputStream, String uploadKey) {

        PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, uploadKey, inputStream, new ObjectMetadata());
        putObjectRequest.setCannedAcl(CannedAccessControlList.PublicRead);
        PutObjectResult putObjectResult = amazonS3Client.putObject(putObjectRequest);
        IOUtils.closeQuietly(inputStream);
        return putObjectResult;

    }

    public List<PutObjectResult> upload(MultipartFile[] multipartFiles) {
        List<PutObjectResult> putObjectResults = new ArrayList<>();
        Arrays.stream(multipartFiles)
                .filter(multipartFile -> !StringUtils.isEmpty(multipartFile.getOriginalFilename()))
                .forEach(multipartFile -> {
                    try {
                        putObjectResults.add(upload(multipartFile.getInputStream(), multipartFile.getOriginalFilename()));
                        System.out.println("FileName:::::*************"+multipartFile.getOriginalFilename());
                        }
                    catch (AmazonServiceException ase) {
                        System.out.println("Caught an AmazonServiceException, which" +
                                " means your request made it " +
                                "to Amazon S3, but was rejected with an error response" +
                                " for some reason.");
                        System.out.println("Error Message:    " + ase.getMessage());
                        System.out.println("HTTP Status Code: " + ase.getStatusCode());
                        System.out.println("AWS Error Code:   " + ase.getErrorCode());
                        System.out.println("Error Type:       " + ase.getErrorType());
                        System.out.println("Request ID:       " + ase.getRequestId());

                    } catch (AmazonClientException ace) {
                        System.out.println("Caught an AmazonClientException, which means"+
                                " the client encountered " +
                                "an internal error while trying to " +
                                "communicate with S3, " +
                                "such as not being able to access the network.");
                        System.out.println("Error Message: " + ace.getMessage());
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                });

        return putObjectResults;
    }

    public ResponseEntity<byte[]> download(String key) throws IOException {
        GetObjectRequest getObjectRequest = new GetObjectRequest(bucket, key);

        S3Object s3Object = amazonS3Client.getObject(getObjectRequest);

        S3ObjectInputStream objectInputStream = s3Object.getObjectContent();

        byte[] bytes = IOUtils.toByteArray(objectInputStream);

        String fileName = URLEncoder.encode(key, "UTF-8").replaceAll("\\+", "%20");

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        httpHeaders.setContentLength(bytes.length);
        httpHeaders.setContentDispositionFormData("attachment", fileName);

        return new ResponseEntity<>(bytes, httpHeaders, HttpStatus.OK);
    }

    public List<S3ObjectSummary> list() {
        ObjectListing objectListing = amazonS3Client.listObjects(new ListObjectsRequest().withBucketName(bucket));

        List<S3ObjectSummary> s3ObjectSummaries = objectListing.getObjectSummaries();

        return s3ObjectSummaries;
    }
}
