package com.archivision.community.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.core.async.AsyncResponseTransformer;
import software.amazon.awssdk.services.s3.S3AsyncClient;

@Component
@RequiredArgsConstructor
@Slf4j
public class S3Service {
    private final S3AsyncClient s3Client;

    public void uploadFileAsBytes(String bucketName, String key, byte[] bytes) {
        s3Client.putObject(req -> req.bucket(bucketName).key(key), AsyncRequestBody.fromBytes(bytes))
                .join();
    }

    public byte[] downloadFileAsBytes(String bucketName, String key) {
        return s3Client.getObject(req -> req.bucket(bucketName).key(key), AsyncResponseTransformer.toBytes())
                .join().asByteArray();
    }
}