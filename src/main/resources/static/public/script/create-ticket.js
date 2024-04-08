const form = document.getElementById('ticket-form');
form.addEventListener('submit', function (event) {
    event.preventDefault();
    submitForm();
});

function submitForm() {
    // TODO flag for draft

    const formData = new FormData();

    const nameInput = document.getElementById('name');
    formData.append('name', nameInput.value);

    const categorySelect = document.getElementById('category');
    formData.append('category', categorySelect.value);
    // TODO send id, not a string

    const descriptionInput = document.getElementById('description');
    formData.append('description', descriptionInput.value);

    const fileInput = document.getElementById('drop-input');
    const files = fileInput.files;
    Array.from(files).forEach((file) => {
        formData.append('files', file);
    });

    const urgencySelect = document.getElementById('urgency');
    formData.append('urgency', urgencySelect.value);
    // TODO send id, not a string

    const commentInput = document.getElementById('comment');
    formData.append('comment', commentInput.value);

    const desiredResolutionDate = document.getElementById('desired-resolution-date');
    formData.append('desiredResolutionDate', desiredResolutionDate.value);

    // TODO Security shit
    fetch('https://localhost:8080/api/tickets', {
        method: 'POST',
        body: formData
    })
        .then(response => {

        })
        .catch(error => {

        });
}

