package com.DreamFactory.DF.cloudinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;


@ExtendWith(MockitoExtension.class)
public class CloudinaryServiceTest {

    private CloudinaryService cloudinaryService;
    private Cloudinary cloudinary;
    private Uploader uploader;

    @BeforeEach
    void setUp() {
        cloudinary = Mockito.mock(Cloudinary.class);
        uploader = Mockito.mock(Uploader.class);
        cloudinaryService = new CloudinaryService(cloudinary);
    }

    @Nested
    class uploadFile{

        @Test
        void when_postImage_from_multipartFile() throws IOException {

            MockMultipartFile image = new MockMultipartFile(
                    "image",
                    "image.jpg",
                    "image/jpeg",
                    "fake-image".getBytes()
            );

            Map<String, String> response = new HashMap<>();
            response.put("secure_url", "https://cloudinary.com/image.jpg");

            Mockito.when(cloudinary.uploader()).thenReturn(uploader);
            Mockito.when(uploader.upload(any(), anyMap())).thenReturn(response);

            Map result = cloudinaryService.uploadFile(image);

            assertEquals("https://cloudinary.com/image.jpg", result.get("secure_url"));

        }
    }

    @Nested
    class deleteFile{

        @Test
        void when_deleteImage_from_publicId() throws IOException{

            Map<String, String> response = new HashMap<>();
            response.put("result", "ok");

            Mockito.when(cloudinary.uploader()).thenReturn(uploader);
            Mockito.when(uploader.destroy(eq("my-image-id"), anyMap())).thenReturn(response);

            assertDoesNotThrow(() -> cloudinaryService.deleteFile("my-image-id"));
        }
    }
}
