let csrfToken;
let accessToken;

async function fetchCsrf() {
    return await fetch("/csrf") .then(response => response.json())
        .then(json => {csrfToken = json})

}

async function refreshToken() {
    if (csrfToken === undefined) {
        await fetchCsrf();
    }

    await fetch(`/auth/refresh?${csrfToken.parameterName}=${csrfToken.token}`, {
        method: "POST"
    }) .then(response => response.json())
        .then(json => {accessToken = json.accessToken})
}

function authorizationHeader() {
    if (accessToken === undefined) {
        refreshToken();
    }

    return `Bearer ${accessToken}`
}

document.addEventListener("DOMContentLoaded",  init)

async function init () {
    await refreshToken();

    if(accessToken !== undefined) {
        renderPage();
        fetchTickets();
    }

}

function fetchTickets() {
    fetch("/api/tickets", {
        headers: {
            'Authorization': authorizationHeader()
        }
    }).then(response => {
        return response.json()
    }).then(json => {
        const tickets = [...json]
        const body = document.querySelector("#table-body");

        tickets.forEach(item => {
            const row = document.createElement("tr");

            row.innerHTML = `
            <td>${item.id}</td>
            <td>${item.name}</td>
            <td>${item.desiredResolutionDate}</td>
            <td>${item.urgency}</td>
            <td>${item.state}</td>
            <td class="actions">
            <select>
                <option value="submit">Submit</option>
            </select>
            <button>▶</button>
        </td>`
            body.appendChild(row);
        })
    })
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
        <button class="button">All Tickets</button>
        <button class="button">My Tickets</button>
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
}