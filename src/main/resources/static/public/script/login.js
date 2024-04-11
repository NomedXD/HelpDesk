let csrfToken;

function fetchCsrf() {
     fetch("/csrf")
        .then(async response => {
            csrfToken = await response.json()
            console.log(csrfToken);
        })
}

function csrfParam() {
    if (csrfToken === undefined) {
        fetchCsrf();
    }

    return `${csrfToken.parameterName}=${csrfToken.token}`
}

function performLogin () {
    const email = document.querySelector("#input-email").value;
    const password = document.querySelector("#input-password").value;

    fetch(`/auth/login?${csrfParam()}`, {
        method: "POST",
        headers: {
            "Authorization": `Basic ${btoa(email + ":" + password)}`
        }
    }).then(response => {
        if(response.ok) {
            return response.json()
                .then(json => location.href="/tickets")
        } else {
            showError("Wrong credentials")
        }
    })
}

document.addEventListener("DOMContentLoaded", () => {
    fetchCsrf();
    document.querySelector("#form-login").addEventListener("submit", (ev) => {
        ev.preventDefault();

        performLogin();
    })
})

function showError(message) {
    const errorDiv = document.querySelector(".error-message");
    errorDiv.style.display = "block";
    errorDiv.innerText = message;

    setTimeout(function() {
        errorDiv.style.display = "none";
    }, 3000);
}