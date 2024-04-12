function showError(message) {
    const errorDiv = document.querySelector(".error-message");
    errorDiv.style.display = "block";
    errorDiv.innerText = message;

    setTimeout(function() {
        errorDiv.style.display = "none";
    }, 3000);
}