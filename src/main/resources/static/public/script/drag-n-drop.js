const dropArea = document.getElementById('drop-area');
const fileInput = document.querySelector('#drop-area .drop-input');

let tempStorage = [];

dropArea.addEventListener('dragover', handleDragOver);
dropArea.addEventListener('dragleave', handleDragLeave);
dropArea.addEventListener('drop', handleDrop);

function handleDragOver(event) {
    event.preventDefault();
    event.dataTransfer.dropEffect = 'copy';
    dropArea.classList.add('highlight');
}

function handleDragLeave() {
    dropArea.classList.remove('highlight');
}

dropArea.addEventListener('drop', handleDrop);
dropArea.addEventListener('click', function (event) {
    if (!event.target.closest('button')) {
        fileInput.click();
    }
});

fileInput.addEventListener('change', handleFileSelection);

function handleDrop(event) {
    event.preventDefault();
    const files = event.dataTransfer.files;
    handleFiles(files);
    removeDeletedFiles();
}

function removeDeletedFiles() {
    const fileContainers = document.querySelectorAll('.file-container');
    const existingFiles = Array.from(fileContainers).map(container => {
        const filename = container.querySelector('.filename').title;
        return tempStorage.find(item => item.file.name === filename).file;
    });

    tempStorage = tempStorage.filter(item => existingFiles.includes(item.file));
}

function handleFileSelection(event) {
    const files = event.target.files;
    handleFiles(files);
    removeDeletedFiles();
}

function handleFiles(files) {
    const existingFileNames = tempStorage.map(item => item.file.name);
    const newFiles = [];

    for (const file of files) {
        if (!existingFileNames.includes(file.name)) {
            const url = URL.createObjectURL(file);
            tempStorage.push({ file, fileData: url });
            newFiles.push(file);
        } else {
            console.log(`File "${file.name}" already exists and will not be added.`);
        }
    }

    previewFiles(newFiles);
}

function previewFiles(newFiles = []) {
    // Clear the existing file previews
    const dropArea = document.getElementById('drop-area');
    dropArea.innerHTML = '';

    for (const { file, fileData } of tempStorage) {
        const fileContainer = document.createElement('div');
        fileContainer.classList.add('file-container');

        const thumbnail = document.createElement('div');
        thumbnail.classList.add('thumbnail');

        const fileExtension = getFileExtension(file.name);
        const fileName = file.name.length > 10 ? file.name.slice(0, 9) + '..' : file.name;

        const deleteButton = document.createElement('button');
        deleteButton.textContent = 'X';
        deleteButton.classList.add('delete-button');
        deleteButton.style.display = 'none';
        deleteButton.addEventListener('click', function (e) {
            e.stopPropagation();
            dropArea.removeChild(fileContainer);

            const index = tempStorage.findIndex((item) => item.file === file);
            if (index !== -1) {
                tempStorage.splice(index, 1);
            }
            removeDeletedFiles();
        });

        if (file.type.startsWith('image/')) {
            const image = document.createElement('img');
            image.src = fileData;
            image.style.width = '90px';
            image.style.height = '90px';
            image.style.borderRadius = '10px';
            image.style.marginRight = '5px';
            image.style.marginLeft = '5px';
            thumbnail.appendChild(image);
        } else {
            const iconImage = document.createElement('img');
            iconImage.src = getFileIcon(fileExtension);
            iconImage.style.width = '90px';
            iconImage.style.height = '90px';
            iconImage.style.marginRight = '5px';
            iconImage.style.marginLeft = '5px';
            thumbnail.appendChild(iconImage);
        }

        const fileNameElement = document.createElement('div');
        fileNameElement.classList.add('filename');
        fileNameElement.textContent = fileName;
        fileNameElement.title = file.name;

        fileContainer.appendChild(deleteButton);
        fileContainer.appendChild(thumbnail);
        fileContainer.appendChild(fileNameElement);

        dropArea.appendChild(fileContainer);

        thumbnail.addEventListener('contextmenu', function (e) {
            e.preventDefault();
            deleteButton.style.display = 'block';
        });

        document.addEventListener('click', function (e) {
            if (!e.target.closest('.file-container')) {
                const deleteButtons = document.querySelectorAll('.delete-button');
                deleteButtons.forEach(button => button.style.display = 'none');
            }
        });
    }

    // Render new files if provided
    if (newFiles.length > 0) {
        handleFiles(newFiles);
    }
}

function getFileExtension(fileName) {
    const parts = fileName.split('.');
    return parts[parts.length - 1].toLowerCase();
}

function getFileIcon(fileExtension) {
    const iconMap = {
        pdf: '/public/icons/pdf-icon.png',
        doc: '/public/icons/doc-icon.png',
        docx: '/public/icons/doc-icon.png',
        xls: '/public/icons/xls-icon.png',
        xlsx: '/public/icons/xls-icon.png',
        ppt: '/public/icons/ppt-icon.png',
        pptx: '/public/icons/ppt-icon.png',
        txt: '/public/icons/txt-icon.png',
        other: '/public/icons/default-icon.png',
    };

    return iconMap[fileExtension] || iconMap['other'];
}