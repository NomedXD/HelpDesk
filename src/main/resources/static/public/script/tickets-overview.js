let allTickets;
let myTickets;

const user = {
    actions: undefined,
    role: undefined,
    email: undefined
};

let currentTab = "all"

const setAllTickets = (tickets) => {allTickets = tickets}
const setMyTickets = (tickets) => {myTickets = tickets}

async function changeTicketStatus(row) {
    const selectedValue = row.querySelector("select").options[row.querySelector("select").selectedIndex].value;;
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
    page.innerHTML = `
    <div class="container">
    <div class="header">
        <h3>Ticket Management System</h3>
        <button class="button">Create New Ticket</button>
    </div>
    <div class="tabs">
        <button class="button" id="all-tickets-button">All Tickets</button>
        <button class="button" id="my-tickets-button">My Tickets</button>
        <button class="refresh-button" id="refresh-tickets-button">↻ Refresh</button>
    </div>
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
            <td>${item.name}</td>
            <td>${item.desiredResolutionDate}</td>
            <td>${item.urgency}</td>
            <td>${item.state}</td>`

            if (user.actions[item.state] !== undefined) {
                const td = document.createElement("td")
                td.className = "actions"

                const select = td.appendChild(document.createElement("select"));

                const button = td.appendChild(document.createElement("button"));
                button.innerText = "▶";

                if( user.role === "ROLE_MANAGER") {
                    if(((item.state === "DRAFT" || item.state === "DECLINED") && user.email === item.ownerEmail)
                        || (item.state === "NEW" && item.ownerRole === "ROLE_EMPLOYEE"))
                    {
                        for (let act in user.actions[item.state]) {

                            const option = document.createElement("option")
                            option.value = `${item.id}:${user.actions[item.state][act]}`;
                            option.innerText = user.actions[item.state][act];

                            select.appendChild(option)

                        }

                        button.addEventListener("click", () => {changeTicketStatus(row)})
                        row.appendChild(td)
                    }
                } else {
                    for (let act in user.actions[item.state]) {

                        const option = document.createElement("option")
                        option.value = `${item.id}:${user.actions[item.state][act]}`;
                        option.innerText = user.actions[item.state][act];

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
    const myTicketsButton = document.querySelector("#my-tickets-button")
    const allTicketsButton = document.querySelector("#all-tickets-button")

    myTicketsButton.style.background = "white"
    myTicketsButton.style.border = "solid 1px black"
    myTicketsButton.style.color = "black"

    allTicketsButton.style.background = "#007bff"
    allTicketsButton.style.border = "none"
    allTicketsButton.style.color = "white"

    while (body.firstChild) {
        body.removeChild(body.firstChild);
    }

    currentTab = "my"
    fetchMyTickets()
}

function switchToAllTickets() {
    console.log("all tickets")

    const body = document.querySelector("#table-body");
    const myTicketsButton = document.querySelector("#my-tickets-button")
    const allTicketsButton = document.querySelector("#all-tickets-button")

    allTicketsButton.style.background = "white"
    allTicketsButton.style.border = "solid 1px black"
    allTicketsButton.style.color = "black"

    myTicketsButton.style.background = "#007bff"
    myTicketsButton.style.border = "none"
    myTicketsButton.style.color = "white"

    while (body.firstChild) {
        body.removeChild(body.firstChild);
    }

    currentTab = "all"
    fetchAllTickets()
}

async function fetchUserInfo() {
    await fetch("/api/user/actions", {
        headers: {
            Authorization: await authorizationHeader()
        }
    }).then(response => response.json())
        .then(json => {user.actions = json})

    await fetch("/api/user/roles", {
        headers: {
            Authorization: await authorizationHeader()
        }
    }).then(response => response.text())
        .then(text => {user.role = text})

    await fetch("/api/user/whoami", {
        headers: {
            Authorization: await authorizationHeader()
        }
    }).then(response => response.text())
        .then(text => {user.email = text})
}