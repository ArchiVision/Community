package com.archivision.community.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.core.async.AsyncResponseTransformer;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.io.File;
import java.nio.file.Path;

@Component
@RequiredArgsConstructor
@Slf4j
public class S3Service {
    private final S3AsyncClient s3Client;

    public void uploadFile(String bucketName, String key, String path) {
        s3Client.putObject(req -> req.bucket(bucketName)
                                        .key(key),
                                AsyncRequestBody.fromFile(new File(path)))
                        .join();
    }

    public void downloadFile(String bucketName, String key, String destinationPath) {
        GetObjectResponse getObjectResponse =
                s3Client.getObject(req -> req.bucket(bucketName)
                                        .key(key),
                                AsyncResponseTransformer.toFile(Path.of(destinationPath)))
                        .join();
    }
}