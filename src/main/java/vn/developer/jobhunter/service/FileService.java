package vn.developer.jobhunter.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartFile;

import vn.developer.jobhunter.util.FileUtil;
import vn.developer.jobhunter.util.error.FileUploadExxceoption;

@Service
public class FileService {

    @Value("${developer.upload-file.base-path}")
    private String basePath;
    @Value("${developer.upload-file.max-size}")
    private DataSize maxSize;

    private final List<String> allowedTypes = Arrays.asList(
            "image/png",
            "image/jpeg",
            "image/jpg",
            "application/pdf",
            "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
    );

    public String uploadFile(MultipartFile file, String folder) {
        try {
            validateFile(file, folder);
            String uploadDir = basePath + folder;
            String fileName = FileUtil.saveFile(uploadDir, file);
            return "/storage/" + folder + "/" + fileName;
        } catch (Exception e) {
            throw new FileUploadExxceoption(e.getMessage());
        }
    }

    private void validateFile(MultipartFile file, String folder) {

        if (file == null || file.isEmpty()) {
            throw new FileUploadExxceoption("File không được để trống");
        }

        if (!allowedTypes.contains(file.getContentType())) {
            throw new FileUploadExxceoption("File type không hợp lệ");
        }

        if (file.getSize() > maxSize.toBytes()) {
            throw new FileUploadExxceoption("File vượt quá 5MB");
        }
        List<String> allowedFolders = List.of("avatar", "company", "resume");

        if (!allowedFolders.contains(folder)) {
            throw new FileUploadExxceoption("Folder không hợp lệ");
        }

    }
}