package com.bearcode.ovf.webservices.s3;

import com.amazonaws.HttpMethod;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.bearcode.ovf.actions.commons.BaseController;
import com.bearcode.ovf.model.system.OvfPropertyNames;
import com.bearcode.ovf.service.OvfPropertyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

@Service
public class S3Service {

    @Autowired
    private OvfPropertyService propertyService;
    @Value("${s3.bucket.name:vote-pdf}")
    private String bucketName;
    @Value("${s3.presigned.url.expire.time:604800000}")
    private long presignedUrlExpireTimeMillis;
    private String env = System.getenv("ENVIRONMENT");
    private String PDF_FILE = ".pdf";
    private AmazonS3 s3Client;
    protected Logger logger = LoggerFactory.getLogger( S3Service.class );

    public void initialiseS3Client() {
        BasicAWSCredentials creds = new BasicAWSCredentials(
                    propertyService.getProperty(OvfPropertyNames.S3_ACCESS_KEY),
                    propertyService.getProperty(OvfPropertyNames.S3_SECRET_KEY)
                );
        logger.warn(propertyService.getProperty(OvfPropertyNames.S3_ACCESS_KEY));
        logger.warn(propertyService.getProperty(OvfPropertyNames.S3_SECRET_KEY));
        s3Client = AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(creds))
                .withRegion(Regions.US_EAST_1)
                .build();
    }

    public PutObjectResult putObject(String key, File file) {
        if (s3Client == null) {
            initialiseS3Client();
        }
        return s3Client.putObject(bucketName, key, file);
    }

    public S3ObjectInputStream getObject(String objectKey) throws Exception {
        if (s3Client == null) {
            initialiseS3Client();
        }
        S3Object s3object = s3Client.getObject(bucketName, objectKey);
        return s3object.getObjectContent();
    }

    public URL getPresignedUrl(String objectKey) {
        if (s3Client == null) {
            initialiseS3Client();
        }
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucketName, getKey(objectKey))
                        .withMethod(HttpMethod.GET)
                        .withExpiration(getPresignatedUrlExpiredTime());
        return s3Client.generatePresignedUrl(generatePresignedUrlRequest);
    }

    private Date getPresignatedUrlExpiredTime() {
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += presignedUrlExpireTimeMillis;
        expiration.setTime(expTimeMillis);
        return expiration;
    }

    public ByteArrayOutputStream getFile(String key) throws Exception {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        IOUtils.copy(getObject(key), buffer);
        return buffer;
    }

    public String getKey(String key) {
        String return_key = env + "-pdf" + key + PDF_FILE;
        // if ("dev".equals(env)) {
        //     return_key = env + key + PDF_FILE;
        // }
        return return_key;
    }
}
