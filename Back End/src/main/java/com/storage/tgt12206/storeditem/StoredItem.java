package com.storage.tgt12206.storeditem;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class StoredItem {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    Integer id;
    
    @Column(name="name")
    String name;
    
    @Column(name="is_folder")
    Boolean isFolder;

    @ManyToOne
    @JoinColumn(name="parent_folder_id")
    StoredItem parentFolder;
}