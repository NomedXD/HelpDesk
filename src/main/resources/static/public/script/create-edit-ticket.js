document.addEventListener("DOMContentLoaded", init)
let state = 'NEW';
let method = 'POST';
const ticketId = location.href.split("/")[location.href.split("/").length - 2];
const path = window.location.pathname.split("/").pop();

const categoryNames = {
    1: 'Application & Services',
    2: 'Benefits & Paper Work',
    3: 'Hardware & Software',
    4: 'People Management',
    5: 'Security & Access',
    6: 'Workplaces & Facilities'
};

async function init() {
    await refreshToken();
    if (accessTokenString !== undefined) {
        createForm();

        if (path === 'edit') {
            await populateForm();
        }
    }
}

function createForm() {
    const body = document.body;

    const header = document.createElement('header');
    const h1 = document.createElement('h1');
    h1.textContent = 'Create Ticket';
    if (path === 'edit') {
        h1.textContent = 'Edit Ticket';
    }
    header.appendChild(h1);
    body.appendChild(header);

    const form = document.createElement('form');
    form.id = 'ticket-form';
    form.classList.add('ticket-form');

    const nameInput = document.createElement('input');
    nameInput.type = 'text';
    nameInput.id = 'name';
    nameInput.name = 'name';
    nameInput.placeholder = 'Title';
    nameInput.required = true;
    form.appendChild(nameInput);

    const categorySelect = document.createElement('select');
    categorySelect.id = 'category';
    categorySelect.name = 'categoryId';
    categorySelect.required = true;

    if (path !== 'edit') {
        const categoryOption = document.createElement('option');
        categoryOption.selected = true;
        categoryOption.disabled = true;
        categoryOption.textContent = 'CATEGORY';
        categorySelect.appendChild(categoryOption);
    }

    for (const [categoryId, categoryName] of Object.entries(categoryNames)) {
        const option = document.createElement('option');
        option.value = categoryId;
        option.textContent = categoryName;
        categorySelect.appendChild(option);
    }

    form.appendChild(categorySelect);

    const descriptionInput = document.createElement('textarea');
    descriptionInput.id = 'description';
    descriptionInput.name = 'description';
    descriptionInput.placeholder = 'Description';
    form.appendChild(descriptionInput);

    const urgencySelect = document.createElement('select');
    urgencySelect.id = 'urgency';
    urgencySelect.name = 'urgency';
    urgencySelect.required = true;
    const urgencyOption = document.createElement('option');
    urgencyOption.selected = true;
    urgencyOption.disabled = true;
    urgencyOption.textContent = 'URGENCY';
    urgencySelect.appendChild(urgencyOption);
    // TODO maybe a separate request to retrieve it from enum instead of hardcode [to ycovich]
    const urgencyOptions = ['CRITICAL', 'HIGH', 'MEDIUM', 'LOW'];
    for (let i = 0; i < urgencyOptions.length; i++) {
        const option = document.createElement('option');
        option.id = i + 1;
        option.value = urgencyOptions[i];
        option.textContent = urgencyOptions[i];
        urgencySelect.appendChild(option);
    }
    form.appendChild(urgencySelect);

    const desiredResolutionDate = document.createElement('input');
    desiredResolutionDate.type = 'text';
    desiredResolutionDate.placeholder = 'DESIRED RESOLUTION DATE';
    desiredResolutionDate.onfocus = function() {
        this.type = 'date';
    };
    desiredResolutionDate.onblur = function() {
        this.type = 'text';
    };
    desiredResolutionDate.id = 'desired-resolution-date';
    desiredResolutionDate.name = 'desiredResolutionDate';
    desiredResolutionDate.required = true;
    form.appendChild(desiredResolutionDate);

    const h3 = document.createElement('h3');
    h3.textContent = 'drop your files below';
    form.appendChild(h3);

    const dropArea = document.createElement('div');
    dropArea.classList.add('drop-area');
    dropArea.id = 'drop-area';

    const dropInput = document.createElement('input');
    dropInput.classList.add('drop-input');
    dropInput.type = 'file';
    dropInput.id = 'drop-input';
    dropInput.multiple = true;
    dropArea.appendChild(dropInput);
    form.appendChild(dropArea);

    const createOrEditButton = document.createElement('button');
    createOrEditButton.classList.add('create-or-edit-button');
    createOrEditButton.type = 'button';
    createOrEditButton.id = 'create-or-edit-button';
    if (path === 'edit') {
        createOrEditButton.textContent = 'edit';
    } else {
        createOrEditButton.textContent = 'submit';
    }
    form.appendChild(createOrEditButton);

    const draftButton = document.createElement('button');
    draftButton.type = 'button';
    draftButton.classList.add('draft-button');
    draftButton.id = 'draft-button';
    draftButton.textContent = 'save as Draft';
    form.appendChild(draftButton);
    draftButton.addEventListener('click', function() {
        state = 'DRAFT';
        submitForm()
            .then(response => {
                if(response.status === 201 || response.status === 202) {
                    tempStorage = [];
                    return response.json();
                } else {
                    showError("Error");
                }
            }).then( json => document.location.href = `/tickets/${json.id}`);
    })
    body.appendChild(form);

    const toTicketOverviewOrTicketListButton = document.createElement('button');
    toTicketOverviewOrTicketListButton.type = 'button';
    toTicketOverviewOrTicketListButton.classList.add('to-ticket-overview-or-ticket-list-button');
    toTicketOverviewOrTicketListButton.id = 'to-ticket-overview-button';
    if (path === 'edit') {
        toTicketOverviewOrTicketListButton.textContent = 'Discard';
    } else {
        toTicketOverviewOrTicketListButton.textContent = 'Ticket List';
    }
    body.appendChild(toTicketOverviewOrTicketListButton);
    toTicketOverviewOrTicketListButton.addEventListener('click', (event) => {
        event.preventDefault();
        if (path === 'edit') {
            location.href = `/tickets/${ticketId}`
        } else {
            location.href = `/tickets`
        }
    })

    const dragNDropScript = document.createElement('script');
    dragNDropScript.src = '/public/script/drag-n-drop.js';
    body.appendChild(dragNDropScript);

    createOrEditButton.addEventListener('click', (event) => {
        event.preventDefault();
        submitForm().then(_ => console.log('something went wrong :('));
    });

}

async function submitForm() {
    const formData = new FormData(document.querySelector("#ticket-form"));
    formData.append('id', ticketId);

    tempStorage.forEach((item) => {
        formData.append('files', item.file, item.file.name);
    });

    formData.append('state', state);

    if (path === 'edit') {
        method = 'PUT';
    }
    return await fetch(`/api/tickets?${csrfParam()}`, {
        method: method,
        headers: {
            Authorization: await authorizationHeader()
        },
        body: formData
    }).then(response => {
        if(response.status === 201 || response.status === 202) {
            tempStorage = [];
            return response.json();
        } else {
            showError("Error");
        }
    }).then( json => document.location.href = `/tickets/${json.id}`);
}

async function populateForm() {
    tempStorage = [];
    const response = await fetch(`/api/tickets/${ticketId}`, {
        headers: {
            Authorization: await authorizationHeader()
        }
    });

    if (response.ok) {
        const ticket = await response.json();

        document.getElementById('name').value = ticket.name;
        const categorySelect = document.getElementById('category');
        const categoryName = categoryNames[ticket.categoryId];
        const categoryOptions = categorySelect.options;
        for (let i = 0; i < categoryOptions.length; i++) {
            if (categoryOptions[i].textContent === categoryName) {
                categoryOptions[i].selected = true;
                break;
            }
        }
        document.getElementById('description').value = ticket.description;
        document.getElementById('urgency').value = ticket.urgency;
        document.getElementById('desired-resolution-date').value = ticket.desiredResolutionDate;

        const filePromises = ticket.attachments.map(async attachment => {
            await downloadFile(attachment.id);
        });

        await Promise.all(filePromises);

        previewFiles();
        if (ticket.state !== 'DRAFT')
        {
            renderErrorPage('Ticket editing is available only if state is draft')
        }
    } else {
        showError('Failed to fetch ticket data');
    }
}

const CHUNK_SIZE = 1024 * 1024; // 1 MB chunks

async function fetchFileChunk(attachmentId, start, length) {
    const response = await fetch(`/api/attachments/${attachmentId}/chunk?start=${start}&length=${length}`, {
        headers: {
            Authorization: await authorizationHeader()
        }
    });

    if (response.ok) {
        return await response.arrayBuffer();
    } else {
        throw new Error(`Failed to fetch file chunk: ${response.status} ${response.statusText}`);
    }
}

async function downloadFile(attachmentId) {
    const response = await fetch(`/api/attachments/${attachmentId}/info`, {
        headers: {
            Authorization: await authorizationHeader()
        }
    });

    if (response.ok) {
        const fileInfo = await response.json();
        const fileSize = fileInfo.size;
        const chunks = [];

        for (let start = 0; start < fileSize; start += CHUNK_SIZE) {
            const length = Math.min(CHUNK_SIZE, fileSize - start);
            const chunkData = await fetchFileChunk(attachmentId, start, length);
            chunks.push(chunkData);
        }

        const blob = new Blob(chunks, { type: fileInfo.type });
        const file = new File([blob], fileInfo.name, { type: fileInfo.type });
        tempStorage.push({ file, fileData: URL.createObjectURL(blob) });
    } else {
        throw new Error(`Failed to fetch file info: ${response.status} ${response.statusText}`);
    }
}


function renderErrorPage(message) {
    const page = document.createElement("div");
    page.className = "center"
    page.innerHTML = `
    <div class="card">
        <button id="go-back-button">Back</button>
        <h3 class="error">${message}</h3>
    </div>`
    document.querySelector("body").innerHTML = page.innerHTML;

    document.querySelector("#go-back-button").addEventListener("click", () => {
        location.href = `/tickets/${ticketId}`
    })
}



