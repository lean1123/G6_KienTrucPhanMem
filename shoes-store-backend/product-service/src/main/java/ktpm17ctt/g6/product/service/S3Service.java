package ktpm17ctt.g6.product.service;

import ktpm17ctt.g6.product.configuration.AWSConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Client s3Client;

    @Value("${app.aws.s3-bucket-name}")
    private String s3BucketName;
    @Value("${app.aws.cloudfront-url}")
    private String cloudfrontUrl;

    /**
     * Upload multiple files to S3 and return CloudFront URLs
     */
    public List<String> uploadFiles(List<MultipartFile> files) throws IOException {
        List<String> urls = new ArrayList<>();

        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                String fileName = generateUniqueFileName(file.getOriginalFilename());
                PutObjectRequest putRequest = PutObjectRequest.builder()
                        .bucket(s3BucketName)
                        .key(fileName)
                        .acl("private")
                        .contentType(file.getContentType())
                        .build();

                // Upload the file to S3
                s3Client.putObject(putRequest, RequestBody.fromBytes(file.getBytes()));

                // Add CloudFront URL to the list
                String fileUrl = cloudfrontUrl + "/" + fileName;
                urls.add(fileUrl);
            }
        }

        return urls;
    }

    private String generateUniqueFileName(String originalFilename) {
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        return UUID.randomUUID().toString() + extension;
    }
}
