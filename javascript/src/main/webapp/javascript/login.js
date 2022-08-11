/**
 * Login management
 */
(function() { // avoid variables ending up in the global scope

   document.getElementById("loginButton").addEventListener('click', function(event) {
      event.preventDefault();
      this.blur();
      var form = document.getElementById("form");
      if (form.checkValidity()) {
         makeCall("POST", "CheckLogin", form,
             function(x) {
                if (x.readyState == XMLHttpRequest.DONE) {
                   var message = x.responseText;
                   switch (x.status) {
                      case 200:
                         if(JSON.parse(message).impiegato) {
                            window.location.href = "homepageImpiegato";
                         } else {
                            window.location.href = "homepageUtente";
                         }
                         break;
                      default:
                         document.getElementById("errorMsg").textContent = message;
                         break;
                   }
                }
             }
         );
      } else {
         form.reportValidity();
      }
   });

})();