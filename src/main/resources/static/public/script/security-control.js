let csrfToken;
let accessToken;
let accessTokenString;

async function fetchCsrf() {
    return await fetch("/csrf") .then(response => response.json())
        .then(json => {csrfToken = json})

}
async function refreshToken() {
    if (csrfToken === undefined) {
        await fetchCsrf();
    }

    return await fetch(`/auth/refresh?${csrfToken.parameterName}=${csrfToken.token}`, {
        method: "POST"
    }) .then(response => {
        if(response.status === 200) {
            return response.json()
        } else if(response.status === 403) {
            location.href = "/auth/login"
        }
    })
        .then(json => {
            accessTokenString = json.accessToken;

            const payloadString = accessTokenString.split(".")[1];
            accessToken = JSON.parse(atob(payloadString));
        })

}
async function authorizationHeader() {
    if (accessTokenString === undefined || isTokenExpired()) {
        await refreshToken();
    }

    return `Bearer ${accessTokenString}`
}
function isTokenExpired () {
    const expirationTimeInSeconds = accessToken.exp; // Expiration time in seconds
    const currentTimeInSeconds = Math.floor(Date.now() / 1000); // Current time in seconds

    return (expirationTimeInSeconds - currentTimeInSeconds < 5);
}