let allTickets;
let myTickets;

let currentTab = "all"

const actionsNames = {
    "NEW": "Submit",
    "APPROVED": "Approve",
    "DECLINED": "Decline",
    "CANCELED": "Cancel",
    "IN_PROGRESS": "Assign to me",
    "DONE": "Set done"
}

const setAllTickets = (tickets) => {allTickets = tickets}
const setMyTickets = (tickets) => {myTickets = tickets}

async function changeTicketStatus(row) {
    const selectedValue = row.querySelector("select").options[row.querySelector("select").selectedIndex].value;
    const ticketId = selectedValue.split(":")[0];
    const newState = selectedValue.split(":")[1];

    return await fetch(`/api/tickets/status?${csrfToken.parameterName}=${csrfToken.token}`, {
        method: "POST",
        headers: {
            'Authorization': await authorizationHeader(),
            'Content-Type': "application/json"
        },
        body: JSON.stringify({
            ticketId: ticketId,
            state: newState
        })
    }).then(_ => {refreshTickets()})
}

document.addEventListener("DOMContentLoaded",  init)
async function init () {
    await refreshToken();
    await fetchUserInfo();

    if(accessTokenString !== undefined) {
        renderPage();
        switchToAllTickets();
    }
}
function renderPage() {
    const page = document.createElement("div");
    page.className = 'container';
    page.innerHTML = `
    <div class="header">
        <button class="refresh-button" id="refresh-tickets-button">↻ Refresh</button>
        <button class="button" id="create-button">Create New Ticket</button>
    </div>
    <div class="tabs">
        <button class="button" id="all-tickets-button">All Tickets</button>
        <button class="button" id="my-tickets-button">My Tickets</button>
    </div>
    <div class="table-container">
        <table class="table">
            <thead>
            <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Desired Date</th>
                <th>Urgency</th>
                <th>Status</th>
                <th>Action</th>
            </tr>
            </thead>
            <tbody id="table-body">
            
            </tbody>
        </table>
    </div>
    
    `
    document.querySelector("body").appendChild(page)

    document.querySelector("#create-button")
        .addEventListener("click", () => {location.href = `/tickets/add`})
    document.querySelector("#my-tickets-button").addEventListener("click", switchToMyTickets)
    document.querySelector("#all-tickets-button").addEventListener("click", switchToAllTickets)

    document.querySelector("#refresh-tickets-button").addEventListener("click", refreshTickets)
}

async function fetchTickets(url, setTickets) {
    fetch(url, {
        headers: {
            Authorization: await authorizationHeader()
        }
    }).then(response => {
        return response.json()
    }).then(json => {
        const tickets = [...json]
        setTickets(tickets)
        const body = document.querySelector("#table-body");

        tickets.forEach(item => {
            const row = document.createElement("tr");

            row.innerHTML = `
            <td>${item.id}</td>
            <td><a href="/tickets/${item.id}">${item.name}</a></td>
            <td>${item.desiredResolutionDate}</td>
            <td>${item.urgency}</td>
            <td>${item.state}</td>`

            if (user.actions[item.state] !== undefined) {
                const td = document.createElement("td")
                td.className = "actions"

                const select = td.appendChild(document.createElement("select"));

                const button = td.appendChild(document.createElement("button"));
                button.className = 'submit-status-button';
                button.innerText = "▶";

                if( user.role === "ROLE_MANAGER") {
                    if(((item.state === "DRAFT" || item.state === "DECLINED") && user.email === item.ownerEmail)
                        || (item.state === "NEW" && item.ownerRole === "ROLE_EMPLOYEE"))
                    {
                        for (let act in user.actions[item.state]) {
                            console.log(user.actions[item.state][act])
                            const option = document.createElement("option")
                            option.value = `${item.id}:${user.actions[item.state][act]}`;
                            option.innerText = actionsNames[user.actions[item.state][act]];

                            select.appendChild(option)
                        }

                        button.addEventListener("click", () => {changeTicketStatus(row)})
                        row.appendChild(td)
                    }
                } else {
                    for (let act in user.actions[item.state]) {
                        if("DONE" === user.actions[item.state][act]) {
                            /*if(item.ownerEmail == null || item.approverEmail == null || item.assigneeEmail == null) {
                                console.log("smth is null")
                                continue
                            }*/
                        }

                        const option = document.createElement("option")
                        console.log(user.actions[item.state][act])
                        option.value = `${item.id}:${user.actions[item.state][act]}`;
                        option.innerText = actionsNames[user.actions[item.state][act]];

                        select.appendChild(option)
                    }

                    button.addEventListener("click", () => {changeTicketStatus(row)})
                    row.appendChild(td)
                }

            }

            body.appendChild(row);
        })
    })
}

async function fetchAllTickets() {
    await fetchTickets("/api/tickets", setAllTickets)
}
async function fetchMyTickets() {
    await fetchTickets("/api/tickets/personal", setMyTickets)
}

function refreshTickets() {
    console.log("refresh")

    const body = document.querySelector("#table-body");
    while (body.firstChild) {
        body.removeChild(body.firstChild);
    }

    if(currentTab === "all") {
        fetchAllTickets();
    } else {
        fetchMyTickets();
    }
}

function switchToMyTickets() {
    console.log("my tickets")
    const body = document.querySelector("#table-body");

    while (body.firstChild) {
        body.removeChild(body.firstChild);
    }

    currentTab = "my"
    fetchMyTickets()
}

function switchToAllTickets() {
    console.log("all tickets")
    const body = document.querySelector("#table-body");

    while (body.firstChild) {
        body.removeChild(body.firstChild);
    }

    currentTab = "all"
    fetchAllTickets()
}