package vn.developer.jobhunter.util;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;
public class FileUtil {

    public static String saveFile(String uploadDir, MultipartFile file) throws IOException {

        File directory = new File(uploadDir);

        if (!directory.exists()) {
            directory.mkdirs();
        }

        String originalName = file.getOriginalFilename();

        String extension = "";

        if (originalName != null && originalName.contains(".")) {
            extension = originalName.substring(originalName.lastIndexOf("."));
        }

        String newFileName = UUID.randomUUID().toString() + extension;

        File destination = new File(uploadDir + File.separator + newFileName);

        file.transferTo(destination);

        return newFileName;
    }
}
