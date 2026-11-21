package com.ryan.loginwithcookie2.util;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Map;

public class CloudinaryUtil {

    private static Cloudinary getCloudinaryInstance() {
        String cloudinaryUrl = EnvConfig.get("CLOUDINARY_URL", "");
        if (!cloudinaryUrl.isEmpty() && !cloudinaryUrl.contains("your_")) {
            return new Cloudinary(cloudinaryUrl);
        }

        String cloudName = EnvConfig.get("CLOUDINARY_CLOUD_NAME", "");
        String apiKey = EnvConfig.get("CLOUDINARY_API_KEY", "");
        String apiSecret = EnvConfig.get("CLOUDINARY_API_SECRET", "");

        if (cloudName.isEmpty() || apiKey.isEmpty() || apiSecret.isEmpty()
                || cloudName.contains("your_") || apiKey.contains("your_") || apiSecret.contains("your_")) {
            return null;
        }

        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret,
                "secure", true
        ));
    }

    /**
     * Upload file ảnh lên Cloudinary bằng Multipart.
     * Nếu chưa cấu hình Cloudinary trong .env thì tự động lưu tạm vào thư mục uploads/.
     */
    @SuppressWarnings("unchecked")
    public static String uploadImage(Part filePart, String contextPath) throws IOException {
        if (filePart == null || filePart.getSize() == 0) {
            return null;
        }

        Cloudinary cloudinary = getCloudinaryInstance();
        if (cloudinary != null) {
            try {
                byte[] fileBytes = filePart.getInputStream().readAllBytes();
                Map<String, Object> uploadResult = cloudinary.uploader().upload(
                        fileBytes,
                        ObjectUtils.asMap(
                                "folder", "loginwithcookie/avatars",
                                "resource_type", "image"
                        )
                );
                Object secureUrl = uploadResult.get("secure_url");
                if (secureUrl != null) {
                    System.out.println("[CloudinaryUtil] Đã upload ảnh lên Cloudinary: " + secureUrl);
                    return secureUrl.toString();
                }
            } catch (Exception e) {
                System.err.println("[CloudinaryUtil] Lỗi upload Cloudinary: " + e.getMessage());
                throw new IOException("Không thể tải ảnh lên Cloudinary: " + e.getMessage(), e);
            }
        }

        // Fallback lưu local khi chưa điền API key Cloudinary trong .env
        System.out.println("[CloudinaryUtil] Chưa cấu hình Cloudinary trong .env, lưu ảnh tạm vào uploads/.");
        String submittedFileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
        String ext = "";
        int idx = submittedFileName.lastIndexOf('.');
        if (idx > 0) {
            ext = submittedFileName.substring(idx);
        }
        String uniqueFileName = "avatar_" + System.currentTimeMillis() + ext;

        File uploadFolder = new File("uploads");
        if (!uploadFolder.exists()) {
            uploadFolder.mkdirs();
        }
        File destFile = new File(uploadFolder, uniqueFileName);
        filePart.write(destFile.getAbsolutePath());
        return (contextPath != null ? contextPath : "") + "/uploads/" + uniqueFileName;
    }
}
