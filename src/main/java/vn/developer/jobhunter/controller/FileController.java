package vn.developer.jobhunter.controller;

import java.time.Instant;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.micrometer.core.ipc.http.HttpSender.Response;
import lombok.RequiredArgsConstructor;
import vn.developer.jobhunter.domain.response.file.ResUploadFileDTO;
import vn.developer.jobhunter.service.FileService;
import vn.developer.jobhunter.util.annotation.ApiMessage;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class FileController {
 private final FileService fileService;

    @PostMapping("/file")
    @ApiMessage("upload single file")
    public ResponseEntity<ResUploadFileDTO> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("folder") String folder )
            {
        String url =  fileService.uploadFile(file, folder);
        return ResponseEntity.ok(new ResUploadFileDTO(url, Instant.now()));
    }
         
   
    
}
