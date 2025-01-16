package com.storage.tgt12206.storeditem;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/api/stored-items")
@CrossOrigin(origins = "http://localhost:3000")
public class StoredItemController {
    private final StoredItemService storedItemService;

    public StoredItemController(StoredItemService storedItemService) {
        this.storedItemService = storedItemService;
    }
    
    @PostMapping("/upload-file")
    public ResponseEntity<?> UploadItem(@RequestParam Integer folderId, @RequestBody MultipartFile file) {
        try {
            ResponseEntity response = ResponseEntity.ok(storedItemService.UploadItem(folderId, file));
            return response;
        } catch (Exception e) {
            return new ResponseEntity(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/create-folder")
    public ResponseEntity<?> UploadItem(@RequestBody UploadFolderDTO dto) {
        try {
            ResponseEntity response = ResponseEntity.ok(storedItemService.UploadItem(dto));
            return response;
        } catch (Exception e) {
            return new ResponseEntity(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get-parent-folder")
    public ResponseEntity<?> GetParentFolder(@RequestParam Integer folderId) {
        try {
            ResponseEntity response = ResponseEntity.ok(storedItemService.GetParentFolder(folderId));
            return response;
        } catch (Exception e) {
            return new ResponseEntity(e, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/get-from")
    public ResponseEntity<?> GetItemsInFolder(@RequestParam Integer folderId) {
        try {
            ResponseEntity response = ResponseEntity.ok(storedItemService.GetItemsInFolder(folderId));
            return response;
        } catch (Exception e) {
            return new ResponseEntity(e, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/download")
    public ResponseEntity<?> DownloadFile(@RequestParam Integer fileId) {
        try {
            return storedItemService.DownloadFile(fileId);
        } catch (Exception e) {
            return new ResponseEntity(e, HttpStatus.NOT_FOUND);
        }
    }
    
}