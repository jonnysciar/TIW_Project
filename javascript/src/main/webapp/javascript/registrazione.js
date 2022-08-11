/**
 * Signup management
 */
(function() { // avoid variables ending up in the global scope

   document.getElementById("signupButton").addEventListener('click', function(event) {
      event.preventDefault();
      this.blur();
      const form = document.getElementById("form");
      if (form.checkValidity()) {
         makeCall("POST", "CheckSignUp", form, function(request) {
                if (request.readyState === XMLHttpRequest.DONE) {
                   const message = request.responseText;
                   if (request.status === 200) {
                      document.getElementById("signupForm").classList.add("d-none")
                      document.getElementById("successMessage").classList.remove("d-none");
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