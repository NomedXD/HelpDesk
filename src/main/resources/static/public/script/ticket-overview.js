const defineTicketId = () => {
    const ticketId = location.href.split("/")[location.href.split("/").length - 1];
    if(!isNaN(parseInt(ticketId))) {
        return parseInt(ticketId);
    } else {
        showError("Oh my, error in url!!!")
    }
}

const ticketId = defineTicketId();
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
toTicketList.addEventListener('click', () => {location.href = "/tickets"})

const toTicketEdit = document.createElement('button');
toTicketEdit.id = 'to-ticket-edit';
toTicketEdit.textContent = 'Edit';
toTicketEdit.addEventListener('click', () => {location.href = `/tickets/${ticketId}/edit`})

const toTicketFeedback = document.createElement('button');
toTicketFeedback.id = 'to-ticket-feedback';
toTicketFeedback.textContent = 'Feedback';
toTicketFeedback.addEventListener('click', () => {location.href = `/tickets/${ticketId}/feedback`})

const errorDiv = document.createElement("div");
errorDiv.className = "error-message"

const header = document.createElement('header');
header.appendChild(toTicketList);
header.appendChild(toTicketFeedback);
header.appendChild(errorDiv);

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

let commentData = [];
let historyData = [];

const ticketInfo = {
    name: 'Name',
    id: 'ID',
    categoryName: 'Category',
    description: 'Description',
    state: 'Status',
    urgency: 'Urgency',
    createdOn: 'Created On',
    desiredResolutionDate: 'Deadline',
    ownerEmail: 'Owner',
    approverEmail: 'Approver',
    assigneeEmail: 'Assignee',
};

async function fetchTicket() {
    if(!ticketId){
        console.log("Error, ticketId is not defined!")
        return;
    }

    return await fetch(`/api/tickets/${ticketId}`, {
        headers: {
            Authorization: await authorizationHeader()
        }
    }).then(response => {
        return response.json()
    }).then(ticket => {
        const ticketTable = document.createElement('table');
        ticketTable.classList.add('ticket-table');
        ticketTable.id = 'ticket-table';
        const tbody = document.createElement('tbody');

        if(ticket.state === "DRAFT"){
            header.appendChild(toTicketEdit);
        }

        for (let field in ticketInfo) {
            const row = document.createElement("tr")
            row.innerHTML = `
            <td class="field">${ticketInfo[field]}</td>
            <td class="value">${ticket[field] == null ? "—" : ticket[field]}</td>`

            console.log(`field: ${field}, row: ${row}`)
            tbody.appendChild(row)
        }

        ticketTable.appendChild(tbody);
        ticketContainer.appendChild(ticketTable);

    })
}

let isCommentTableOpen = false;
let isHistoryTableOpen = false;

let currentTab = "none"

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
            <td class="td-comment-center">${comment.date.split("T")[0]}</td>
            <td class="td-comment-center">${comment.userEmail}</td>
            <td class="td-comment-justify">${comment.text}</td>
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
            <td class="td-history">${entry.date.split("T")[0]}</td>
            <td class="td-history">${entry.userEmail}</td>
            <td class="td-history">${entry.action}</td>
            <td class="td-history">${entry.description}</td>
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

async function fetchComments() {
    return await fetch(`/api/tickets/${ticketId}/comments`, {
        headers: {
            Authorization: await authorizationHeader()
        }
    }).then(response => {
        return response.json()
    }).then(json => {commentData = [...json]})
}
async function fetchHistory() {
    return await fetch(`/api/tickets/${ticketId}/histories`, {
        headers: {
            Authorization: await authorizationHeader()
        }
    }).then(response => {
        return response.json()
    }).then(json => {historyData = [...json]})
}

async function postComment(text) {
    return await fetch(`/api/comments?${csrfParam()}`, {
        method: "POST",
        headers: {
            "Authorization": await authorizationHeader(),
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            ticketId:ticketId,
            text: text
        })
    }).then(response => {
        if(response.ok) {
            return response.json()
                .then(comment => {
                    commentData.push(comment)
                    renderCommentTable();
                })
        } else {
            showError("Wrong comment text")
        }
    })
}

async function init() {
    await refreshToken();

    if(accessToken) {
        await fetchTicket();
        await fetchComments();
        await fetchHistory();
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

    postCommentButton.addEventListener('click', async () => {
        const text = document.querySelector("#comment-input").value;
        if(text.length < 1){
            showError("Wrong comment text")
        } else {
            await postComment(text)
        }
    })
}

document.addEventListener("DOMContentLoaded", init);