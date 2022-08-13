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
         //TODO change event handler, AJAX call
         console.log(row.id);
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