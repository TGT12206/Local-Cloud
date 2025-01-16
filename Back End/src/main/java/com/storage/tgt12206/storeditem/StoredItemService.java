package com.storage.tgt12206.storeditem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class StoredItemService {
    private final static String STORAGE_FOLDER = "C:\\Users\\thoma\\App Stuff\\Storage App\\Back End\\Storage\\";

    private final StoredItemRepository storedItemRepository;

    public StoredItemService(StoredItemRepository storedItemRepository) {
        this.storedItemRepository = storedItemRepository;
    }

    public String UploadItem(Integer folderId, MultipartFile file) throws IOException {
        StoredItem newItem = new StoredItem();
        newItem.name = file.getOriginalFilename();
        newItem.isFolder = false;
        newItem.parentFolder = storedItemRepository.findById(folderId).orElseThrow();

        storedItemRepository.save(newItem);
        
        // Setting up the path of the file
        String filePath = STORAGE_FOLDER + newItem.id;
        try {
            File newFile = new File(filePath);
            newFile.createNewFile();
            FileOutputStream fout = new FileOutputStream(newFile);
            fout.write(file.getBytes());
            fout.close();
        } catch (Exception e) {
            storedItemRepository.delete(newItem);
            throw e;
        }
        
        return "File Uploaded Successfully";
    }

    public String UploadItem(UploadFolderDTO dto) {
        StoredItem newItem = new StoredItem();
        newItem.name = dto.name;
        newItem.isFolder = true;
        newItem.parentFolder = storedItemRepository.findById(dto.parentFolderId).orElseThrow();
        storedItemRepository.save(newItem);

        return "Folder Uploaded Successfully";
    }

    public GetItemsDTO GetParentFolder(Integer folderId) {
        StoredItem folder = storedItemRepository.findById(folderId).orElseThrow();
        if (folder.parentFolder == null) {
            return null;
        }
        GetItemsDTO output = new GetItemsDTO();
        output.id = folder.parentFolder.id;
        output.name = folder.parentFolder.name;
        output.isFolder = true;
        output.parentId = folder.parentFolder.id;
        return output;
    }

    public List<GetItemsDTO> GetItemsInFolder(Integer folderId) {
        StoredItem folder = storedItemRepository.findById(folderId).orElseThrow();
        List<StoredItem> items = storedItemRepository.findByParentFolder(folder);
        List<GetItemsDTO> output = new LinkedList<>();
        for (StoredItem storedItem : items) {
            GetItemsDTO newItem = new GetItemsDTO();
            newItem.id = storedItem.id;
            newItem.name = storedItem.name;
            newItem.isFolder = storedItem.isFolder;
            newItem.parentId = storedItem.parentFolder.id;
            output.add(newItem);
        }
        return output;
    }

    public ResponseEntity<?> DownloadFile(Integer id) throws FileNotFoundException {
        StoredItem item = storedItemRepository.findById(id).orElseThrow();
        String filePath = STORAGE_FOLDER + item.id;
        File file = new File(filePath);
        InputStreamResource fileData = new InputStreamResource(new FileInputStream(file));
        String headerValue = "attachment; filename=\"" + item.name + "\"";
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
            .body(fileData);
    }
}