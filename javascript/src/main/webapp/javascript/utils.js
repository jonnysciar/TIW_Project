function setLogout() {
    document.getElementById("logoutButton").addEventListener("click", function(event) {
       event.preventDefault();
       makeCall("GET", "logout", null, function(request) {
           if (request.readyState === XMLHttpRequest.DONE) {
               if (request.status === 200) {
                   sessionStorage.clear();
                   console.log("Client session cleared");
                   window.location.href = "./";
               }
           }
       });
    });
}

function validateEmail(input) {
    const validRegex = /^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/;
    return !!input.value.match(validRegex);
}

function makeCall(method, url, formElement, cback, reset = true) {
    const request = new XMLHttpRequest(); // visible by closure
    request.onreadystatechange = function() {
        cback(request)
    }; // closure
    request.open(method, url);
    if (formElement == null) {
        request.send();
    } else {
        request.send(new FormData(formElement));
    }
    if (formElement !== null && reset === true) {
        formElement.reset();
    }
}