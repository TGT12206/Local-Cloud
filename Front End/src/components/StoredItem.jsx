import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';

function StoredItem(props) {
    const handleOnClick = async () => {
        if (props.item.isFolder) {
            window.location.href=`/folder/${props.item.id}`;
        } else{
            await props.handleDownload(props.item);
        }
    }
    return (
        <div className="item" onClick={handleOnClick}>
            {props.isParentFolder && `Parent Folder: ${props.item.name}`}
            {!props.isParentFolder && props.item.name}
        </div>
    );
}

export default StoredItem;