/**
 * User homepage management
 */
(function() { // avoid variables ending up in the global scope

   const utente = JSON.parse(sessionStorage.getItem("user"));

   document.getElementById("welcomeText").textContent = "Benvenuto " + utente.nome +", ecco i preventivi da gestire:";
   setLogout();

   makeCall("GET", "getHomepage", null, function(request) {
      if (request.readyState === XMLHttpRequest.DONE) {
         const message = request.responseText;
         if (request.status === 200) {
            const array = JSON.parse(message);
            setPreventiviPrezzati(array[0]);
            setPreventiviDaPrezzare(array[1]);
         } else {
            document.getElementById("errorMsg").textContent = message;
         }
      }
   });

})();

function setPreventiviPrezzati(preventivi) {
   const tbody = document.querySelector("#pricedTable tbody");
   tbody.innerHTML = "";
   const fields = ["id", "nomeProdotto"];
   preventivi.forEach((preventivo) => {
      let row = document.createElement("tr");
      let col = document.createElement("td");
      col.textContent = "Completato";
      col.style.color = "green";

      row.id = "row" + preventivo.id;
      detailOnClick(row);
      row.addEventListener("click", function() {
         document.getElementById("detailText").textContent = "Ecco i dettagli del preventivo richiesto:";
         document.getElementById("errorMsg2").textContent = "";
      });
      fields.forEach((field) => {
         let col = document.createElement("td");
         col.textContent = preventivo[field];
         row.appendChild(col);
      });
      row.appendChild(col);
      tbody.appendChild(row);
   });
}

function setPreventiviDaPrezzare(preventivi) {
   const tbody = document.querySelector("#toBePricedTable tbody");
   tbody.innerHTML = "";
   const fields = ["id", "nomeProdotto"];

   preventivi.forEach((preventivo) => {
      let row = document.createElement("tr");
      let col = document.createElement("td");
      col.textContent = "Da completare";
      col.style.color = "red";

      row.id = "rowToBePriced" + preventivo.id;
      detailOnClick(row);
      row.removeEventListener("click", rowOnClick);
      row.addEventListener("click", function(event) {
         event.preventDefault();
         document.getElementById("errorMsg2").textContent = "";
         document.getElementById("detailText").textContent = "Ecco i dettagli del preventivo da prezzare:";
         const url = "prezzaPreventivo?id=" + this.id.substring(13,this.id.length);
         makeCall("GET", url, null,function(request) {
            if (request.readyState === XMLHttpRequest.DONE) {
               const message = request.responseText;
               const errorMsg = document.getElementById("errorMsg");
               errorMsg.textContent = "";
               if (request.status === 200) {
                  this.mainDiv.style.filter = "blur(4px)";
                  this.detailDiv.classList.remove("d-none");
                  this.mainDiv.style.pointerEvents = "none";
                  setToBePriced(JSON.parse(message));
               } else if (request.status === 400 || request.status === 500) {
                  errorMsg.textContent = message;
               } else {
                  errorMsg.textContent = "Server error!"
               }
            }
         });
      });

      fields.forEach((field) => {
         let col = document.createElement("td");
         col.textContent = preventivo[field];
         row.appendChild(col);
      });
      row.appendChild(col);
      tbody.appendChild(row);
   });
}

function setToBePriced(array) {
   const prodotto = array[0];
   const preventivo = array[1];

   const optionTbody = document.getElementById("detailOptionTable").getElementsByTagName("tbody")[0];
   const statusTbodyTable = document.getElementById("detailStatusTable");
   statusTbodyTable.classList.add("table-borderless");

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

   let col = document.createElement("th");
   col.textContent = "Inserisci Prezzo";
   col.style.color = "black";
   statusHead.appendChild(col);

   //Create input field
   let row = document.createElement("tr");
   col = document.createElement("td");
   let input =document.createElement("input");
   input.type = "number";
   input.classList.add("form-control");
   input.required = true;
   input.id = input.name = "price";
   col.appendChild(input);
   row.appendChild(col);
   statusBody.appendChild(row);

   //Create button
   row = document.createElement("tr");
   col = document.createElement("td");
   let button = document.createElement("button");
   button.type = "submit";
   button.classList.add("btn", "btn-warning");
   button.textContent = "Invia Prezzo";
   col.appendChild(button);
   row.appendChild(col);
   statusBody.appendChild(row);

   button.addEventListener("click", function(event) {
      event.preventDefault();
      this.blur();

      const form = this.closest("form");
      let price = parseInt(input.value);
      if (!isNaN(price) && price > 0) {
         console.log("Server call");
      } else {
         document.getElementById("errorMsg2").textContent = "Il prezzo inserito non è valido!";
      }
   });

}