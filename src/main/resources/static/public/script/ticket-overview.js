const commentButton = document.createElement('button');
commentButton.id = 'comment-button';
commentButton.textContent = 'comments';

const postCommentButton = document.createElement('button');
postCommentButton.id = 'post-comment-button';
postCommentButton.textContent = 'post';

const historyButton = document.createElement('button');
historyButton.id = 'history-button';
historyButton.textContent = 'history';

const toTicketList = document.createElement('button');
toTicketList.id = 'to-ticket-list';
toTicketList.textContent = 'Ticket List';

const toTicketEdit = document.createElement('button');
toTicketEdit.id = 'to-ticket-edit';
toTicketEdit.textContent = 'Edit';

const toTicketFeedback = document.createElement('button');
toTicketFeedback.id = 'to-ticket-feedback';
toTicketFeedback.textContent = 'Feedback';

const header = document.createElement('header');
header.appendChild(toTicketList);
header.appendChild(toTicketEdit);
header.appendChild(toTicketFeedback);

const ticketContainer = document.createElement('div');
ticketContainer.classList.add('ticket-container');
ticketContainer.id = 'ticket-container';

const tableButtons = document.createElement('div');
tableButtons.classList.add('table-buttons');
tableButtons.id = 'table-buttons';
tableButtons.appendChild(historyButton);
tableButtons.appendChild(commentButton);


const tableContainer = document.createElement('div');
tableContainer.classList.add('table-container');
tableContainer.id = 'table-container';
tableContainer.style.display = 'none';

const body = document.querySelector('body')
body.appendChild(header);
body.appendChild(ticketContainer);
body.appendChild(tableButtons);
body.appendChild(tableContainer);

const commentData = [
    { date: '2024-03-15', user: 'ycovich', comment: 'test comment' },
    { date: '2024-03-16', user: 'VladK27', comment: 'really long test comment really long test comment ' +
            'really long test comment really long test comment really long test comment ' +
            'really long test comment really long test comment really long test comment ' +
            'really long test comment really long test comment really long test comment ' +
            'really long test comment really long test comment really long test comment ' +
            'really long test comment really long test comment really long test comment ' +
            'really long test comment really long test comment really long test comment' },
];
const historyData = [
    { date: '2024-03-15', user: 'ycovich', action: 'created ticket', description: 'ticket for NomedXD' },
    { date: '2024-03-16', user: 'VladK27', action: 'approved & assigned ticket', description: '' },
];

const ticketInfo = {
    title: 'Title',
    id: 'ID',
    category: 'Category',
    description: 'Description',
    status: 'Status',
    urgency: 'Urgency',
    createdOn: 'Created On',
    desiredResolutionDate: 'Deadline',
    attachments: 'Attachments',
    owner: 'Owner',
    approver: 'Approver',
    assignee: 'Assignee',
};

let isCommentTableOpen = false;
let isHistoryTableOpen = false;

function renderCommentTable() {
    const table = document.createElement('table');
    table.classList.add('comment-table');
    table.id = 'comment-table';

    table.innerHTML = `
    <thead>
      <tr>
        <th>Date</th>
        <th>User</th>
        <th>Comment</th>
      </tr>
    </thead>
    <tbody>
      ${commentData
        .map(
            (comment) => `
          <tr>
            <td class="td-comment-center">${comment.date}</td>
            <td class="td-comment-center">${comment.user}</td>
            <td class="td-comment-justify">${comment.comment}</td>
          </tr>
        `
        )
        .join('')}
    </tbody>
  `;

    const commentInputContainer = document.createElement('div');
    const textarea = document.createElement('textarea');
    commentInputContainer.classList.add('comment-input-container');
    textarea.classList.add('comment-input');
    commentInputContainer.id = 'comment-input-container';
    textarea.id = 'comment-input';


    textarea.setAttribute('placeholder', 'add comment');
    commentInputContainer.innerHTML = '';
    commentInputContainer.appendChild(textarea);

    tableContainer.innerHTML = '';
    tableContainer.appendChild(table);
    tableContainer.appendChild(commentInputContainer);
    tableContainer.appendChild(postCommentButton);
    tableContainer.style.display = 'block';
    isCommentTableOpen = true;
    isHistoryTableOpen = false;
}

function renderHistoryTable() {
    const table = document.createElement('table');
    table.classList.add('history-table');
    table.id = 'history-table';
    table.innerHTML = `
    <thead>
      <tr>
        <th>Date</th>
        <th>User</th>
        <th>Action</th>
        <th>Description</th>
      </tr>
    </thead>
    <tbody>
      ${historyData
        .map(
            (entry) => `
          <tr>
            <td class="td-history">${entry.date}</td>
            <td class="td-history">${entry.user}</td>
            <td class="td-history">${entry.action}</td>
            <td>${entry.description}</td>
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
    const ticketTable = document.createElement('table');
    ticketTable.classList.add('ticket-table');
    ticketTable.id = 'ticket-table';
    ticketTable.innerHTML = `
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
    ticketContainer.appendChild(ticketTable);
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

// TODO EventListener for PostCommentButton

renderTicketInfo();
