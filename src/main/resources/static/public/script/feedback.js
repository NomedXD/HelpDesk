let rating;
const ticketId = location.href.split("/")[location.href.split("/").length - 2];
let ticket;
let feedback;

let stars;
let output;

    function gfg(n) {
        remove();
        for (let i = 0; i < n; i++) {
            if (n == 1) cls = "one";
            else if (n == 2) cls = "two";
            else if (n == 3) cls = "three";
            else if (n == 4) cls = "four";
            else if (n == 5) cls = "five";
            stars[i].className = "star " + cls;
        }

        output.innerText = "Rating is: " + n + "/5";
        rating = n;
    }

    function remove() {
        let i = 0;
        while (i < 5) {
            stars[i].className = "star";
            i++;
        }
    }

    document.addEventListener("DOMContentLoaded", init)

    async function init() {
        await refreshToken();
        await fetchUserInfo();

        await fetchFeedback();
        await fetchTicket();
        console.log([accessTokenString, user, ticket, feedback, accessToken])
        if (ticket === undefined) {
            console.log(404)
            renderErrorPage("Ticket not found")
            return;
        }
        if (ticket.state !== "DONE") {
            console.log("ticket is not in DONE state")
            renderErrorPage("Can't leave feedback, because ticket is not in DONE state yet!")
            return;
        }
        if (feedback === undefined) {
            if (ticket.ownerEmail !== user.email) {
                console.log("No feedback yet")
                renderErrorPage("No feedback yet");
                return;
            }
        }
        if (ticket.ownerEmail == null || ticket.approverEmail == null || ticket.assigneeEmail == null) {
            console.log("Some requirements not completed");
            renderErrorPage("Can't leave feedback yet. Please check is approver and assignee are present!")
            return;
        }
        if (accessTokenString !== undefined) {
            console.log("Token is defined")
            if (feedback === undefined) {
                renderFeedbackCreation();
            } else {
                renderFeedbackView();
            }
        }
    }


    function renderFeedbackCreation() {
        const page = document.createElement("div");
        page.className = "center"
        page.innerHTML = `<div class="card">
    <button id="go-back-button">Back</button>
    <h1>Ticket(${ticketId}) - ${ticket.name}</h1>
    <br/>
        <div hidden class="error-message"></div>
    <span onclick="gfg(1)"
          class="star">★
        </span>
    <span onclick="gfg(2)"
          class="star">★
        </span>
    <span onclick="gfg(3)"
          class="star">★
        </span>
    <span onclick="gfg(4)"
          class="star">★
        </span>
    <span onclick="gfg(5)"
          class="star">★
        </span>
    <h3 id="output">
        Rating is: 0/5
    </h3>
    <div class="feedback-info">
        <textarea placeholder="Addition comments" id="comment"></textarea>
        <button id="submit-button">Submit</button>
    </div>
</div>
    `
        document.querySelector("body").appendChild(page)
        document.querySelector("#go-back-button").addEventListener("click", () => {
            location.href = `/tickets/${ticket.id}`
        })
        document.querySelector("#submit-button").addEventListener("click", async () => {
            await sendFeedback()
        });
        stars = document.getElementsByClassName("star");
        output = document.getElementById("output");

    }

    function renderFeedbackView() {
        console.log("VIEW")
        const page = document.createElement("div");
        page.className = "center"
        page.innerHTML = `<div class="card">
    <button id="go-back-button">Back</button>
    <h1>Ticket(${ticketId}) - ${ticket.name}</h1>
    <br/>
    <span class="star">★</span>
    <span class="star">★</span>
    <span class="star">★</span>
    <span class="star">★</span>
    <span class="star">★</span>

    <span>
        <h3 id="output">
            Rating is: 0/5
        </h3>
    </span>
    
    <div class="feedback-info">
    <div>Comment:</div>
        <div class="comment" id="comment">${feedback.text}</textarea>
    </div>
</div>
    `
        document.querySelector("body").appendChild(page)
        document.querySelector("#go-back-button").addEventListener("click", () => {
            location.href = `/tickets/${ticket.id}`
        })
        stars = document.getElementsByClassName("star");
        output = document.getElementById("output");
        gfg(feedback.rate);
    }
    function renderErrorPage(message) {
        const page = document.createElement("div");
        page.className = "center"
        page.innerHTML = `
    <div class="card">
        <button id="go-back-button">Back</button>
        <h3 class="error-message">${message}</h3>
    </div>`
        document.querySelector("body").appendChild(page)

        document.querySelector("#go-back-button").addEventListener("click", () => {
            location.href = `/tickets/${ticket.id}`
        })
    }

    async function fetchTicket() {
        return await fetch(`/api/tickets/${ticketId}`, {
            headers: {
                Authorization: await authorizationHeader()
            }
        }).then(response => {
            if (response.status === 404) {
                location.href = "/error/404"
            } else {
                return response.json()
            }
        }).then(json => {
            ticket = json
        })
    }

    async function fetchFeedback() {
        return await fetch(`/api/tickets/${ticketId}/feedback`, {
            headers: {
                Authorization: await authorizationHeader()
            }
        }).then(response => {
            if (response.status === 404) {
                feedback = undefined;
                return undefined;
            } else {
                return response.json()
            }
        }).then(json => {
            feedback = json;
        })
    }

    async function sendFeedback() {
        if(rating === 0 || rating === undefined) {
            showError("Provide rate please")
            return;
        }
        const comment = document.querySelector("#comment").value;
        const commentRegex = new RegExp("^([aA-zZ]|[0-9]|~|\\.|\"|\\(|\\)|:|;|\\||<|>|@|\\[|]|!|#|\\$|%|&|'|\\*|\\+|-|/|=|\\?|\\^|_|`|\\{|}| )*$")
        if(!commentRegex.test(comment)) {
            showError("Comment contains unavailable characters")
            return;
        }
        return await fetch(`/api/tickets/${ticketId}/feedback?${await csrfParam()}`, {
            method: "POST",
            headers: {
                Authorization: await authorizationHeader(),
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                rate: rating,
                text: document.querySelector("#comment").value,
                ticketId: ticketId
            })
        }).then(response => {
            if(response.status === 201) {
                return response.json();
            } else {
                showError("Error")
            }
        }).then(json => {
            feedback = json;
            console.log(json)
            document.querySelector("body").innerHTML = "";
            renderFeedbackView()
        })
    }

function showError(message) {
    const errorDiv = document.querySelector(".error-message");
    errorDiv.style.display = "block";
    errorDiv.innerText = message;

    setTimeout(function() {
        errorDiv.style.display = "none";
    }, 3000);
}