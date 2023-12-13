package com.example.onlinemedicine.service;

import com.example.onlinemedicine.entity.MedicineEntity;
import com.example.onlinemedicine.entity.PhotoEntity;
import com.example.onlinemedicine.exception.DataNotFoundException;
import com.example.onlinemedicine.repository.PhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class PhotoService {
    private final PhotoRepository repository;
    private final String fileStorage = "src\\main\\resources\\photos";
    private final Path fileStoragePath = Paths.get(fileStorage).toAbsolutePath().normalize();

    public String uploadImageToFileSystem(MultipartFile file, MedicineEntity medicine) throws IOException {
        String originalFilename = file.getOriginalFilename();
        PhotoEntity attachment = new PhotoEntity();
        attachment.setMedicine(medicine);
        attachment.setName(originalFilename);
        try {
            Path targetLocation = fileStoragePath.resolve(originalFilename);
            Files.copy(file.getInputStream(), Path.of(targetLocation+File.separator));
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
        filePath = fileStorage + "/" + filePath;
        Path path = Paths.get(filePath);
        if (Files.exists(path)) {
            return path.toString();
        }
        throw new DataNotFoundException("PHOTO NOT FOUND");
    }
}
