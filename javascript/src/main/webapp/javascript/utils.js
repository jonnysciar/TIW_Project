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

function makeCall(method, url, formElement, callback, reset = true) {
    const request = new XMLHttpRequest(); // visible by closure
    request.onreadystatechange = function() {
        callback(request)
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

function detailOnClick(row) {
    const detailDiv = document.getElementById("detailDiv");
    const mainDiv = document.getElementById("mainDiv");
    const detailButton = document.getElementById("detailButton");

    detailButton.addEventListener("click", function(event) {
        event.preventDefault();
        this.blur();
        mainDiv.style.filter = "blur(0px)";
        detailDiv.classList.add("d-none");
        mainDiv.style.pointerEvents = "auto";
    });

    row.style.cursor = "pointer";
    row.mainDiv = mainDiv;
    row.detailDiv = detailDiv;
    row.addEventListener("click", rowOnClick);
}

function rowOnClick(event) {
    event.preventDefault();

    let id = parseInt(this.id.substring(3,this.id.length));
    const errorMsg = document.getElementById("errorMsg");

    if (!isNaN(id)) {
        const url = "dettagliPreventivo?id=" + this.id.substring(3, this.id.length);
        makeCall("GET", url, null, function (request) {
            if (request.readyState === XMLHttpRequest.DONE) {
                const message = request.responseText;
                errorMsg.textContent = "";
                if (request.status === 200) {
                    this.mainDiv.style.filter = "blur(4px)";
                    this.detailDiv.classList.remove("d-none");
                    this.mainDiv.style.pointerEvents = "none";
                    setPrevDetails(JSON.parse(message));
                } else if (request.status === 400 || request.status === 500) {
                    errorMsg.textContent = message;
                } else {
                    errorMsg.textContent = "Server error!";
                }
            }
        });
    } else {
        errorMsg.textContent = "Errore nella richiesta!";
    }
}

function setPrevDetails(array) {
    const prodotto = array[0];
    const preventivo = array[1];
    let nomeImpiegato;
    if (array.length !== 3) {
        nomeImpiegato = null;
    } else {
        nomeImpiegato = array[2];
    }

    const optionTbody = document.getElementById("detailOptionTable").getElementsByTagName("tbody")[0];
    const statusTbodyTable = document.getElementById("detailStatusTable");
    statusTbodyTable.classList.remove("table-borderless");

    optionTbody.innerHTML = "";

    document.getElementById("detailProductName").textContent = prodotto.nome;
    document.getElementById("detailImg").src = "./images/" + prodotto.imgPath;
    document.getElementById("detailPrevNumber").textContent = "Preventivo numero: #" + preventivo.id;

    preventivo.opzioni.forEach((opzione) => {
        let row = document.createElement("tr");
        let colName = document.createElement("td");
        let colType = document.createElement("td");
        colName.textContent = opzione.nome;
        colType.textContent = opzione.tipo.toLowerCase().replace("_", " ");
        row.appendChild(colName);
        row.appendChild(colType);
        optionTbody.appendChild(row);
    });

    const statusHead = statusTbodyTable.getElementsByTagName("thead")[0].getElementsByTagName("tr")[0];
    const statusBody = statusTbodyTable.getElementsByTagName("tbody")[0];
    statusHead.innerHTML = "";
    statusBody.innerHTML = ""

    if (nomeImpiegato !== null) {
        let col = document.createElement("th");
        col.textContent = "Completato";
        col.style.color = "green";
        statusHead.appendChild(col);

        let row = document.createElement("tr");
        col = document.createElement("td");
        col.textContent = "Prezzo: " + preventivo.prezzo;
        row.appendChild(col);
        statusBody.appendChild(row);

        row = document.createElement("tr");
        col = document.createElement("td");
        col.textContent = "Username impiegato: " + nomeImpiegato;
        row.appendChild(col);
        statusBody.appendChild(row);

    } else {
        const col = document.createElement("th");
        col.textContent = "Da completare";
        col.style.color = "red";
        statusHead.appendChild(col);
    }
}