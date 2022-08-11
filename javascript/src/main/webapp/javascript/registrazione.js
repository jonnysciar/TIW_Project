/**
 * Signup management
 */
(function() { // avoid variables ending up in the global scope

   document.getElementById("signupButton").addEventListener('click', function(event) {
      event.preventDefault();
      this.blur();
      var form = document.getElementById("form");
      if (form.checkValidity()) {
         makeCall("POST", "CheckSignUp", form,
             function(x) {
                if (x.readyState == XMLHttpRequest.DONE) {
                   var message = x.responseText;
                   switch (x.status) {
                      case 200:
                         document.getElementById("signupForm").classList.add("d-none")
                         document.getElementById("successMessage").classList.remove("d-none");
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