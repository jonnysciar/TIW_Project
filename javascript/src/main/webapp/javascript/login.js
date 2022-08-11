/**
 * Login management
 */
(function() { // avoid variables ending up in the global scope

   document.getElementById("loginButton").addEventListener('click', function(event) {
      event.preventDefault();
      this.blur();
      const form = document.getElementById("form");
      if (form.checkValidity()) {
         makeCall("POST", "CheckLogin", form, function(request) {
                if (request.readyState === XMLHttpRequest.DONE) {
                   const message = request.responseText;
                   if (request.status === 200) {
                      const utente = JSON.parse(message);
                      sessionStorage.setItem("user", message);
                      if (utente.impiegato) {
                         window.location.href = "homepageImpiegato";
                      } else {
                         window.location.href = "homepageUtente";
                      }
                   } else {
                      document.getElementById("errorMsg").textContent = message;
                   }
                }
             }
         );
      } else {
         form.reportValidity();
      }
   });

})();