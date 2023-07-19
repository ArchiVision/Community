package com.archivision.community.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class ResourceLoaderService {
    private final ResourceLoader resourceLoader;

    public byte[] getDefaultImage() {
        Resource resource = resourceLoader.getResource("classpath:no_image.jpg");
        try {
            return resource.getContentAsByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
