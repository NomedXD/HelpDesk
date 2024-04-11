window.onload = function() {
    createForm();
    const form = document.getElementById('ticket-form');
    const urlParams = new URLSearchParams(window.location.search);
    const ticketId = urlParams.get('ticketId');

    if (ticketId) {
        fetchTicketData(ticketId);
    }

    form.addEventListener('submit', function (event) {
        event.preventDefault();
        if (ticketId) {
            submitForm(ticketId);
        } else {
            // exception ?
        }
    });
}

function fetchTicketData(ticketId) {
    fetch(`https://localhost:8080/api/tickets/${ticketId}`)
        .then(response => response.json())
        .then(data => {
            // Populate the form fields with the ticket data
            document.getElementById('name').value = data.name;
            document.getElementById('category').value = data.category;
            document.getElementById('description').value = data.description;
            document.getElementById('urgency').value = data.urgency;
            document.getElementById('desired-resolution-date').value = data.desiredResolutionDate;

            // Fetch and display attachments
            fetchAttachments(data.attachments);
        })
        .catch(error => console.error('Error:', error));
}

function fetchAttachments(attachments) {
    attachments.forEach(attachment => {
        fetch(attachment.url)
            .then(response => response.blob())
            .then(blob => {
                const file = new File([blob], attachment.name, { type: attachment.type });
                handleFiles([file]);
            })
            .catch(error => console.error('Error:', error));
    });
}

function createForm() {
    const body = document.body;

    // Create header
    const header = document.createElement('header');
    const h1 = document.createElement('h1');
    h1.textContent = 'Edit Ticket';
    header.appendChild(h1);
    body.appendChild(header);

    // Create form
    const form = document.createElement('form');
    form.id = 'ticket-form';
    form.classList.add('ticket-form');

    // Create form elements
    const nameInput = document.createElement('input');
    nameInput.type = 'text';
    nameInput.id = 'name';
    nameInput.name = 'name';
    nameInput.placeholder = 'Title';
    nameInput.required = true;
    form.appendChild(nameInput);
    form.appendChild(document.createElement('br'));
    form.appendChild(document.createElement('br'));

    const categorySelect = document.createElement('select');
    categorySelect.id = 'category';
    categorySelect.name = 'category';
    categorySelect.required = true;
    const categoryOption = document.createElement('option');
    categoryOption.selected = true;
    categoryOption.disabled = true;
    categoryOption.textContent = 'CATEGORY';
    categorySelect.appendChild(categoryOption);
    const options = ['Application &amp; Services', 'Benefits &amp; Paper Work', 'Hardware &amp; Software', 'People Management', 'Security &amp; Access', 'Workplaces &amp; Facilities'];
    for (let i = 0; i < options.length; i++) {
        const option = document.createElement('option');
        option.value = i + 1;
        option.textContent = options[i];
        categorySelect.appendChild(option);
    }
    form.appendChild(categorySelect);
    form.appendChild(document.createElement('br'));
    form.appendChild(document.createElement('br'));

    const descriptionInput = document.createElement('textarea');
    descriptionInput.id = 'description';
    descriptionInput.name = 'description';
    descriptionInput.placeholder = 'Description';
    form.appendChild(descriptionInput);
    form.appendChild(document.createElement('br'));
    form.appendChild(document.createElement('br'));

    const urgencySelect = document.createElement('select');
    urgencySelect.id = 'urgency';
    urgencySelect.name = 'urgency';
    urgencySelect.required = true;
    const urgencyOption = document.createElement('option');
    urgencyOption.selected = true;
    urgencyOption.disabled = true;
    urgencyOption.textContent = 'URGENCY';
    urgencySelect.appendChild(urgencyOption);
    const urgencyOptions = ['CRITICAL', 'HIGH', 'MEDIUM', 'LOW'];
    for (let i = 0; i < urgencyOptions.length; i++) {
        const option = document.createElement('option');
        option.id = i + 1;
        option.value = urgencyOptions[i];
        option.textContent = urgencyOptions[i];
        urgencySelect.appendChild(option);
    }
    form.appendChild(urgencySelect);
    form.appendChild(document.createElement('br'));
    form.appendChild(document.createElement('br'));

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
    desiredResolutionDate.required = true;
    form.appendChild(desiredResolutionDate);
    form.appendChild(document.createElement('br'));
    form.appendChild(document.createElement('br'));

    const h3 = document.createElement('h3');
    h3.textContent = 'drop your files here';
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
    form.appendChild(document.createElement('br'));
    form.appendChild(document.createElement('br'));

    const editButton = document.createElement('button');
    editButton.type = 'button';
    editButton.classList.add('submit-button');
    editButton.id = 'edit-button';
    editButton.textContent = 'edit';
    form.appendChild(editButton);

    const draftButton = document.createElement('button');
    draftButton.type = 'button';
    draftButton.classList.add('draft-button');
    draftButton.id = 'draft-button';
    draftButton.textContent = 'save as draft';
    form.appendChild(draftButton);

    const toTicketOverviewButton = document.createElement('button');
    toTicketOverviewButton.type = 'button';
    toTicketOverviewButton.classList.add('to-ticket-overview-button');
    toTicketOverviewButton.id = 'to-ticket-overview-button';
    toTicketOverviewButton.textContent = 'Ticket Overview';
    body.appendChild(toTicketOverviewButton);

    body.appendChild(form);

    // Load drag-n-drop.js
    const dragNDropScript = document.createElement('script');
    dragNDropScript.src = 'drag-n-drop.js';
    body.appendChild(dragNDropScript);
}


function submitForm(ticketId) {
    const formData = new FormData();

    const nameInput = document.getElementById('name');
    formData.append('name', nameInput.value);

    const categorySelect = document.getElementById('category');
    formData.append('categoryId', categorySelect.value);

    const descriptionInput = document.getElementById('description');
    formData.append('description', descriptionInput.value);

    const fileInput = document.getElementById('drop-input');
    const files = fileInput.files;
    Array.from(files).forEach((file) => {
        formData.append('files', file);
    });

    const urgencySelect = document.getElementById('urgency');
    formData.append('urgency', urgencySelect.value);

    const desiredResolutionDate = document.getElementById('desired-resolution-date');
    formData.append('desiredResolutionDate', desiredResolutionDate.value);

    fetch(`https://localhost:8080/api/tickets/${ticketId}`, {
        method: 'PUT',
        body: formData
    })
        .then(response => {
            if (response.ok) {
                console.log('Ticket updated successfully');
                // Optionally, you can redirect the user or display a success message
            } else {
                console.error('Error updating ticket');
                // Optionally, you can display an error message
            }
        })
        .catch(error => console.error('Error:', error));
}

