package com.example.onlinemedicine.service;

import com.example.onlinemedicine.entity.MedicineEntity;
import com.example.onlinemedicine.entity.PhotoEntity;
import com.example.onlinemedicine.repository.PhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.file.Files;
import java.util.Optional;
import java.util.UUID;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class PhotoService {
    private final PhotoRepository repository;
    private final String FOLDER_PATH = "/src/main/resources/uploads";

    public void uploadImageToFileSystem(MultipartFile file, MedicineEntity medicine) throws IOException {
        String filePath = FOLDER_PATH + file.getOriginalFilename();

        PhotoEntity fileData = repository.save(PhotoEntity.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .filePath(filePath)
                .medicine(medicine).build());

        file.transferTo(new File(filePath));

    }

    public byte[] downloadImageFromFileSystem(UUID medicineId) throws IOException {
        Optional<PhotoEntity> fileData = repository.findByMedicineId(medicineId);
        String filePath = fileData.get().getFilePath();
        byte[] images = Files.readAllBytes(new File(filePath).toPath());
        return images;
    }

}
