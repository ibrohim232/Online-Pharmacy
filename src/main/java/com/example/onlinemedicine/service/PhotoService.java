package com.example.onlinemedicine.service;

import com.example.onlinemedicine.entity.MedicineEntity;
import com.example.onlinemedicine.entity.PhotoEntity;
import com.example.onlinemedicine.exception.DataNotFoundException;
import com.example.onlinemedicine.repository.PhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PhotoService {
    private final PhotoRepository repository;

    // Use a configurable environment variable for file storage path
    private final String fileStorage = System.getenv("FILE_STORAGE_PATH") != null ?
            System.getenv("FILE_STORAGE_PATH") :
            "src" + File.separator + "main" + File.separator + "resources" + File.separator + "photos";

    private final Path fileStoragePath = Paths.get(fileStorage).toAbsolutePath().normalize();

    public String uploadImageToFileSystem(MultipartFile file, MedicineEntity medicine) throws IOException {
        String originalFilename = file.getOriginalFilename();
        PhotoEntity attachment = new PhotoEntity();
        attachment.setMedicine(medicine);
        attachment.setName(originalFilename);
        try {
            Path targetLocation = fileStoragePath.resolve(originalFilename);
            Files.copy(file.getInputStream(), targetLocation);
            attachment.setFilePath(targetLocation.toAbsolutePath().toString());
        } catch (FileAlreadyExistsException e) {
            String fileName = originalFilename.substring(0, originalFilename.lastIndexOf(".")) + medicine.getId()
                    + originalFilename.substring(originalFilename.lastIndexOf("."));
            Path targetLocation = fileStoragePath.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation);
            attachment.setFilePath(targetLocation.toAbsolutePath().toString());
        }
        repository.save(attachment);
        return attachment.getFilePath();
    }

    public String downloadImageFromFileSystem(UUID medicineId) throws IOException {
        PhotoEntity photoNotFound = repository.findByMedicineId(medicineId).orElseThrow(() -> new DataNotFoundException("PHOTO NOT FOUND"));
        return photoNotFound.getFilePath();
    }

    public String downloadImage(String filePath) throws IOException {
        filePath = fileStorage + File.separator + filePath;
        Path path = Paths.get(filePath);
        if (Files.exists(path)) {
            return path.toString();
        }
        throw new DataNotFoundException("PHOTO NOT FOUND");
    }
}
