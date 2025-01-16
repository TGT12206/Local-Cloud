import React, { useEffect, useState } from 'react';
import StoredItem from '../StoredItem';
import { useParams } from 'react-router-dom';

function Home() {
    const { folderId } = useParams();
    const [parentFolder, setParentFolder] = useState(null);
    const [newFolderName, setNewFolderName] = useState(null);
    const [items, setItems] = useState(null);
    const [file, setFile] = useState(null);

    const handleNewFolderNameChange = (e) => {
        setNewFolderName(e.target.value);
    }

    const handleFileChange = (e) => {
        const file = e.target.files[0];
        setFile(file);
    }

    useEffect(() => {
        fetchFolder();
    }, [folderId] );

    const fetchFolder = async () => {
        try {
            const response = await fetch(`http://localhost:8080/api/stored-items/get-from?folderId=${folderId}`, {
                method: 'GET'
            });
            const data = await response.json();
            setItems(data);
            await fetchParentFolder();
            const status = response.status;
            console.log(status);
        } catch (error) {
            console.log('error: ', error);
        }
    }

    const fetchParentFolder = async () => {
        try {
            const response = await fetch(`http://localhost:8080/api/stored-items/get-parent-folder?folderId=${folderId}`, {
                method: 'GET'
            });
            const data = await response.json();
            setParentFolder(data);
            console.log(data);
            const status = response.status;
            console.log(status);
        } catch (error) {
            console.log('error: ', error);
        }
    }

    const handleUploadFile = async () => {
        if (file === null) {
            return;
        }
        try {
            const formData = new FormData();
            formData.append("file", file);
            const response = await fetch(`http://localhost:8080/api/stored-items/upload-file?folderId=${folderId}`, {
                method: 'POST',
                body: formData
            });
            const status = response.status;
            console.log(status);
            window.location.reload();
        } catch (error) {
            console.log('error: ', error);
        }
    }

    const handleCreateFolder = async () => {
        if (newFolderName === null) {
            return;
        }
        try {
            const reqBody = {
                'name': newFolderName,
                'parentFolderId': folderId
            }
            const response = await fetch(`http://localhost:8080/api/stored-items/create-folder`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(reqBody)
            });
            const status = response.status;
            console.log(status);
            window.location.reload();
        } catch (error) {
            console.log('error: ', error);
        }
    }

    const handleDownloadFile = async (item) => {
        try {
            const response = await fetch(`http://localhost:8080/api/stored-items/download?fileId=${item.id}`, {
                method: 'GET',
                responseType: "blob",
                headers: {
                    'Content-Type': 'application/octet-stream',
                }
            });
            const status = response.status;
            const blob = await response.blob();
            if (status === 200) {
                const url = window.URL.createObjectURL(blob);

                // Create a temporary <a> element to trigger the download
                const tempLink = document.createElement("a");
                tempLink.href = url;
                tempLink.setAttribute(
                'download',
                `${item.name}`
                ); // Set the desired filename for the downloaded file

                // Append the <a> element to the body and click it to trigger the download
                document.body.appendChild(tempLink);
                tempLink.click();

                // Clean up the temporary elements and URL
                document.body.removeChild(tempLink);
                window.URL.revokeObjectURL(url);
            }
        } catch (error) {
            console.log('error: ', error);
        }
    }

    return (
        <div className="background">
            <div className="page-container">
                {parentFolder !== null &&
                    <div>
                        <label>Parent Folder</label>
                        <StoredItem
                            key={parentFolder.id}
                            item={parentFolder}
                            isParent={true}
                            handleDownload={handleDownloadFile}
                        >
                        </StoredItem>
                    </div>
                }
                <label>New Folder Name</label>
                <input type='text' onChange={handleNewFolderNameChange}></input>
                <button onClick={handleCreateFolder}>Create Folder</button>
                <input type='file' onChange={handleFileChange}></input>
                <button onClick={handleUploadFile}>Upload File</button>
                {items !== null && items.length > 0 &&
                    items.map((item) => (
                        <StoredItem
                            key={item.id}
                            item={item}
                            isParent={false}
                            handleDownload={handleDownloadFile}
                        >
                        </StoredItem>
                    ))
                }
            </div>
        </div>
    );
}

export default Home;