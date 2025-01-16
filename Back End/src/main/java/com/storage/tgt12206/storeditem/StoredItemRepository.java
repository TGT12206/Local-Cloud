package com.storage.tgt12206.storeditem;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoredItemRepository extends JpaRepository<StoredItem, Integer> {

    @Override
    Optional<StoredItem> findById(Integer id);
    
    List<StoredItem> findByParentFolder(StoredItem ParentFolder);

}