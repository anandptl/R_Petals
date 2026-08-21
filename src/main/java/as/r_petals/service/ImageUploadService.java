package as.r_petals.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class ImageUploadService {

    private final Cloudinary cloudinary;

    public ImageUploadService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public String upload(MultipartFile file, String folder) {
        return uploadWithDetails(file, folder).url();
    }

    public UploadResult uploadWithDetails(MultipartFile file, String folder) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Image file is required");
        }

        try {
            Map<?, ?> result = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "folder", folder,
                            "resource_type", "image"
                    )
            );

            Object secureUrl = result.get("secure_url");
            Object publicId = result.get("public_id");

            if (secureUrl == null || publicId == null) {
                throw new RuntimeException("Cloudinary did not return image details");
            }

            return new UploadResult(
                    secureUrl.toString(),
                    publicId.toString()
            );

        } catch (IOException e) {
            throw new RuntimeException("Image upload failed", e);
        }
    }

    public void deleteByPublicId(String publicId) {
        if (publicId == null || publicId.isBlank()) {
            return;
        }

        try {
            cloudinary.uploader().destroy(
                    publicId,
                    ObjectUtils.asMap(
                            "resource_type", "image",
                            "type", "upload",
                            "invalidate", true
                    )
            );
        } catch (Exception e) {
            throw new RuntimeException("Cloudinary image delete failed", e);
        }
    }

    public record UploadResult(String url, String publicId) {
    }
}
