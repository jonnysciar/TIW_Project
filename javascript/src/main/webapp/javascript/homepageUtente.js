/**
 * User homepage management
 */
(function() { // avoid variables ending up in the global scope

   const utente = JSON.parse(sessionStorage.getItem("user"));
   document.getElementById("welcomeText").textContent = "Benvenuto " + utente.nome +", ecco cosa puoi fare:";
   setLogout();

   makeCall("GET", "getHomepage", null, function (request) {
      if (request.readyState === XMLHttpRequest.DONE) {
         const message = request.responseText;
         if (request.status === 200) {
            const array = JSON.parse(message);
            console.log(array[0]);
            console.log(array[1]);
            sessionStorage.setItem("prodotti", JSON.stringify(array[0]));
            setProdotti(array[0]);
            setPreventivi(array[1]);
         } else {
            document.getElementById("errorMsg").textContent = message;
         }
      }
   });

})();

function setProdotti(prodotti) {
   const selectProduct = document.getElementById("selectProduct");
   prodotti.forEach((prodotto) => {
      let option = document.createElement("option");
      option.value = prodotto.id;
      option.text = prodotto.nome;
      selectProduct.appendChild(option);
   });
}

function setPreventivi(preventivi) {
   const tbody = document.querySelector("#preventiviTable tbody");
   const fields = ["id", "nomeProdotto", "prezzo"];
   preventivi.forEach((preventivo) => {
      let row = document.createElement("tr");
      fields.forEach((field) => {
         console.log(field);
         let col = document.createElement("td");
         if (field !== "prezzo") {
            col.textContent = preventivo[field];
         } else {
            if (preventivo[field] > 0) {
               let p = document.createElement("p");
               p.textContent = "Completato";
               p.style.color = "green";
               col.appendChild(p);
               p = document.createElement("p");
               p.textContent = "Prezzo: " + preventivo[field];
               col.appendChild(p);
            } else {
               col.textContent = "Da completare";
               col.style.color = "red";
            }
         }
         row.appendChild(col);
         tbody.appendChild(row);
      });
   });
}