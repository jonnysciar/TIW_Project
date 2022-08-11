/**
 * User homepage management
 */
(function() { // avoid variables ending up in the global scope
   window.addEventListener("load", function(event) {
      setLogout();
      const utente = JSON.parse(sessionStorage.getItem("user"));
      document.getElementById("welcomeText").textContent = "Benvenuto " + utente.nome +", ecco cosa puoi fare:";
      makeCall("GET", "getHomepage", null, function (request) {
         if (request.readyState === XMLHttpRequest.DONE) {
            const message = request.responseText;
            if (request.status === 200) {
               console.log(JSON.parse(message));
            } else {
               document.getElementById("errorMsg").textContent = message;
            }
         }
      });
   });

})();