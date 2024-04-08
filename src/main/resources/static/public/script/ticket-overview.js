
const commentButton = document.getElementById('comment-button');
const historyButton = document.getElementById('history-button');
const ticketContainer = document.getElementById('ticket-container');
const tableContainer = document.getElementById('table-container');
tableContainer.style.display = 'none';

const commentData = [
    { id: 1, author: 'John', text: 'Great post!' },
    { id: 2, author: 'Jane', text: 'I disagree.' },
];
const historyData = [
    { id: 1, event: 'Logged in', date: '2024-03-15' },
    { id: 2, event: 'Updated profile', date: '2024-03-16' },
];

const ticketInfo = {
    title: 'Title',
    createdOn: 'Created On',
    status: 'Status',
    category: 'Category',
    urgency: 'Urgency',
    desiredResolutionDate: 'Desired Resolution Date',
    owner: 'Owner',
    approver: 'Approver',
    assignee: 'Assignee',
    attachments: 'Attachments',
    description: 'Description',
};

let isCommentTableOpen = false;
let isHistoryTableOpen = false;

function renderCommentTable() {
    const table = document.createElement('table');
    table.classList.add('comment-table');
    table.innerHTML = `
    <thead>
      <tr>
        <th>ID</th>
        <th>Author</th>
        <th>Text</th>
      </tr>
    </thead>
    <tbody>
      ${commentData
        .map(
            (comment) => `
          <tr>
            <td>${comment.id}</td>
            <td>${comment.author}</td>
            <td>${comment.text}</td>
          </tr>
        `
        )
        .join('')}
    </tbody>
  `;
    tableContainer.innerHTML = '';
    tableContainer.appendChild(table);
    tableContainer.style.display = 'block';
    isCommentTableOpen = true;
    isHistoryTableOpen = false;
}

function renderHistoryTable() {
    const table = document.createElement('table');
    table.classList.add('history-table');
    table.innerHTML = `
    <thead>
      <tr>
        <th>ID</th>
        <th>Event</th>
        <th>Date</th>
      </tr>
    </thead>
    <tbody>
      ${historyData
        .map(
            (entry) => `
          <tr>
            <td>${entry.id}</td>
            <td>${entry.event}</td>
            <td>${entry.date}</td>
          </tr>
        `
        )
        .join('')}
    </tbody>
  `;
    tableContainer.innerHTML = '';
    tableContainer.appendChild(table);
    tableContainer.style.display = 'block';
    isHistoryTableOpen = true;
    isCommentTableOpen = false;
}

function renderTicketInfo() {
    const ticketInfoTable = document.createElement('table');
    ticketInfoTable.classList.add('ticket-table')
    ticketInfoTable.innerHTML = `
    <tbody>
      ${Object.entries(ticketInfo)
        .map(
            ([_, fieldName]) => `
          <tr>
            <td class="field">${fieldName}</td>
            <td class="value">blank</td>
          </tr>
        `
        )
        .join('')}
    </tbody>
  `;
    ticketContainer.innerHTML = '';
    ticketContainer.appendChild(ticketInfoTable);
}

commentButton.addEventListener('click', () => {
    if (isCommentTableOpen) {
        tableContainer.style.display = 'none';
        isCommentTableOpen = false;
    } else {
        renderCommentTable();
    }
});

historyButton.addEventListener('click', () => {
    if (isHistoryTableOpen) {
        tableContainer.style.display = 'none';
        isHistoryTableOpen = false;
    } else {
        renderHistoryTable();
    }
});


const commentInputContainer = document.getElementById('comment-input-container');
function renderCommentInput() {
    const textarea = document.createElement('textarea');
    textarea.classList.add('comment-input');
    textarea.setAttribute('placeholder', 'Enter your comment');
    commentInputContainer.innerHTML = '';
    commentInputContainer.appendChild(textarea);
    commentInputContainer.style.display = 'block';
}


renderTicketInfo();
