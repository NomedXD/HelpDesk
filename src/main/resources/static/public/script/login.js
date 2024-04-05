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
            return response.json().then(json => {
                localStorage.setItem("token", json.accessToken);
                fetch("/api/tickets",{
                    headers: {
                        "Authorization": "Bearer " + json.accessToken
                    }
                }).then(response => {
                    return response.json()
                        .then(json => location.href="/tickets")
                })
            })
        }
    }).catch(error => {
        document.querySelector(".error-message").removeAttribute("hidden");
        console.log(error)
    })
}

document.addEventListener("DOMContentLoaded", () => {
    fetchCsrf();
    document.querySelector("#form-login").addEventListener("submit", (ev) => {
        ev.preventDefault();

        performLogin();
    })
})
