const dropArea = document.getElementById('drop-area');
const fileInput = document.querySelector('#drop-area .drop-input');

const tempStorage = [];

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
}

function handleFileSelection(event) {
    const files = event.target.files;
    handleFiles(files);
}

function handleFiles(files) {
    for (const file of files) {
        const reader = new FileReader();
        reader.onload = function (event) {
            const fileData = event.target.result;
            tempStorage.push({ file, fileData });

            const fileContainer = document.createElement('div');
            fileContainer.classList.add('file-container');

            const thumbnail = document.createElement('div');
            thumbnail.classList.add('thumbnail');

            const fileExtension = getFileExtension(file.name);
            const fileName = file.name.length > 10 ? file.name.slice(0, 9) + '..' : file.name;

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

            const deleteButton = document.createElement('button');
            deleteButton.textContent = 'X';
            deleteButton.style.display = 'none';
            deleteButton.addEventListener('click', function (e) {
                e.stopPropagation();
                dropArea.removeChild(fileContainer);

                const index = tempStorage.findIndex((item) => item.file === file);
                if (index !== -1) {
                    tempStorage.splice(index, 1);
                }
            });

            fileContainer.appendChild(thumbnail);
            fileContainer.appendChild(fileNameElement);
            fileContainer.appendChild(deleteButton);

            dropArea.appendChild(fileContainer);

            thumbnail.addEventListener('contextmenu', function (e) {
                e.preventDefault();
                deleteButton.style.display = 'inline-block';
            });

            fileNameElement.addEventListener('mouseenter', function (e) {
                const popUp = document.createElement('div');
                popUp.classList.add('popup');
                popUp.textContent = file.name;
                popUp.style.top = `${e.clientY - 20}px`;
                popUp.style.left = `${e.clientX + 10}px`;

                document.body.appendChild(popUp);

                fileNameElement.addEventListener('mouseleave', function () {
                    document.body.removeChild(popUp);
                });
            });


            document.addEventListener('click', function (e) {
                if (!e.target.closest('.file-container')) {
                    deleteButton.style.display = 'none';
                }
            });
        };
        reader.readAsDataURL(file);
    }
}

function getFileExtension(fileName) {
    const parts = fileName.split('.');
    return parts[parts.length - 1].toLowerCase();
}

function getFileIcon(fileExtension) {
    // TODO add icons
    const iconMap = {
        pdf: 'pdf.png',
        doc: 'doc-icon.png',
        docx: 'doc-icon.png',
        xls: 'xls-icon.png',
        xlsx: 'xls-icon.png',
        ppt: 'ppt-icon.png',
        pptx: 'ppt-icon.png',
        txt: 'txt-icon.png',
        other: 'default-icon.png',
    };

    return iconMap[fileExtension] || iconMap['other'];
}