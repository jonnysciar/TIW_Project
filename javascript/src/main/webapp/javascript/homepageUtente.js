/**
 * User homepage management
 */
(function() { // avoid variables ending up in the global scope

   const utente = JSON.parse(sessionStorage.getItem("user"));
   let prodotti;

   document.getElementById("welcomeText").textContent = "Benvenuto " + utente.nome +", ecco cosa puoi fare:";
   setLogout();

   makeCall("GET", "getHomepage", null, function(request) {
      if (request.readyState === XMLHttpRequest.DONE) {
         const message = request.responseText;
         if (request.status === 200) {
            const array = JSON.parse(message);
            prodotti = array[0];
            setProdotti(prodotti);
            setPreventivi(array[1]);
         } else {
            document.getElementById("errorMsg").textContent = message;
         }
      }
   });

})();

function setProdotti(prodotti) {
   const selectProduct = document.getElementById("selectProduct");
   const img = document.getElementById("productImg");
   const table = document.getElementById("optionTable");
   const button = document.getElementById("preventivoButton");
   const errorMsg = document.getElementById("errorMsg");

   prodotti.forEach((prodotto) => {
      let option = document.createElement("option");
      option.value = prodotto.id;
      option.text = prodotto.nome;
      selectProduct.appendChild(option);
   });

   selectProduct.addEventListener("change", function(event) {
      event.preventDefault();
      errorMsg.textContent = "";
      const prodotto = prodotti[this.value - 1];
      img.src = "./images/" + prodotto.imgPath;
      const tbody = table.getElementsByTagName("tbody")[0];
      tbody.innerHTML = "";
      const fields = ["nome", "tipo", "codice"]
      prodotto.opzioni.forEach((opzione) => {
         let row = document.createElement("tr");
         fields.forEach((field) => {
            let col = document.createElement("td");
            if (field !== "codice") {
               col.textContent = opzione[field].toLowerCase().replace("_", " ");
            } else {
               const checkbox = document.createElement("input");
               checkbox.type = "checkbox";
               checkbox.value = opzione[field];
               checkbox.name = "checkbox";
               col.appendChild(checkbox);
            }
            row.appendChild(col);
            tbody.appendChild(row);
         });
      });

      img.closest("div").classList.remove("d-none");
      table.closest("div").classList.remove("d-none");
      button.closest("div").classList.remove("d-none");
   });

   button.addEventListener("click", function(event) {
      event.preventDefault();
      this.blur();
      const form = this.closest("form");
      errorMsg.textContent = "";
      if (oneOptionChecked(form.getElementsByTagName("input"))) {
         makeCall("POST", "CheckPreventivo", form, function (request) {
            if (request.readyState === XMLHttpRequest.DONE) {
               const message = request.responseText;
               if (request.status === 200) {
                  img.closest("div").classList.add("d-none");
                  table.closest("div").classList.add("d-none");
                  button.closest("div").classList.add("d-none");
                  form.reset();
                  setPreventivi(JSON.parse(message));
               } else if (request.status === 400 || request.status ===500) {
                  errorMsg.textContent = message;
               } else {
                  errorMsg.textContent = "Server error!"
               }
            }
         }, false);
      } else {
         errorMsg.textContent = "Nessuna opzione è stata scelta";
      }
   });

}

function setPreventivi(preventivi) {
   const tbody = document.querySelector("#preventiviTable tbody");
   tbody.innerHTML = "";
   const fields = ["id", "nomeProdotto", "prezzo"];
   preventivi.forEach((preventivo) => {
      let row = document.createElement("tr");
      row.id = "row" + preventivo.id;
      detailOnClick(row);
      fields.forEach((field) => {
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

function oneOptionChecked(checkboxes) {
   for (let i = 0; i < checkboxes.length; i++) {
      if (checkboxes[i].checked) {
         return true;
      }
   }
   return false;
}