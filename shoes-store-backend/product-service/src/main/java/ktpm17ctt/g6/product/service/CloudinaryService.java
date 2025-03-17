package ktpm17ctt.g6.product.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Slf4j
public class CloudinaryService {
    Cloudinary cloudinary;

    @Async
    public Map uploadFile(MultipartFile file, String folder, String fileName) throws IOException {
        Map uploadParams = ObjectUtils.asMap(
                "async", "auto",
                "folder", "ShoesShopApp/"+folder,
                "public_id", fileName
        );
        return cloudinary.uploader().upload(file.getBytes(), uploadParams);
    }

    public Map deleteFile(String publicId) throws IOException {
        return cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
    }

    @Async
    public List<Map> uploadFiles(List<MultipartFile> files, String folder, String fileName) throws IOException {
        List<Map> uploadResults = new ArrayList<>();
        System.out.println("Uploading files: " + files.size());
        for (MultipartFile file : files) {
            if (file.isEmpty()) continue;
            String uniqueSuffix = UUID.randomUUID().toString().substring(0, 8);
            String uniqueFileName = fileName + "_" + uniqueSuffix;
            Map uploadParams = ObjectUtils.asMap(
                    "folder", "ShoesShopAppV2/" + folder,
                    "public_id", uniqueFileName
            );
            try {
                Map uploadResult = cloudinary.uploader().upload(file.getBytes(), uploadParams);
                uploadResults.add(uploadResult);
                System.out.println("Uploaded URL: " + uploadResult.get("url"));
            } catch (Exception e) {
                System.err.println("Error uploading file: " + e.getMessage());
            }
        }
        System.out.println("Uploaded files: " + uploadResults.size());
        return uploadResults;
    }
}
