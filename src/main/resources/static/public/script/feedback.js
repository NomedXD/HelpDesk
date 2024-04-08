let rating;
// To access the stars

let stars;
let output;

document.addEventListener("DOMContentLoaded", () => {
    stars =
        document.getElementsByClassName("star");
    output =
        document.getElementById("output");
    }
)
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
}

// To remove the pre-applied styling
function remove() {
    let i = 0;
    while (i < 5) {
        stars[i].className = "star";
        i++;
    }
}

document.addEventListener("DOMContentLoaded",  init)
async function init () {
    await refreshToken();
    await fetchUserInfo();

    if(accessTokenString !== undefined) {
        renderPage();
    }
}

function renderPage() {
    const page = document.createElement("div");
    page.innerHTML = `<div class="card">
    <button>Back</button>
    <h1>Ticket(1) - name</h1>
    <br/>
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
        <textarea placeholder="Addition comments"></textarea>
        <button>Submit</button>
    </div>
</div>
    `
    document.querySelector("body").appendChild(page)

    document.querySelector("#my-tickets-button").addEventListener("click", switchToMyTickets)
    document.querySelector("#all-tickets-button").addEventListener("click", switchToAllTickets)

    document.querySelector("#refresh-tickets-button").addEventListener("click", refreshTickets)
}