/**
 * Signup management
 */
(function() { // avoid variables ending up in the global scope

   document.getElementById("signupButton").addEventListener("click", function(event) {
      event.preventDefault();
      this.blur();
      const form = document.getElementById("form");
      const email = document.getElementById("email");
      const password = document.getElementById("password");
      const password2 = document.getElementById("password2");
      const errorMsg = document.getElementById("errorMsg");

      if (!validateEmail(email)) {
         errorMsg.textContent = "Email non valida";
      } else if (!password.value.match(password2.value)){
         errorMsg.textContent = "Le password inserite non coincidono";
      } else {
         makeCall("POST", "CheckSignUp", form, function(request) {
                if (request.readyState === XMLHttpRequest.DONE) {
                   const message = request.responseText;
                   if (request.status === 200) {
                      document.getElementById("signupForm").classList.add("d-none")
                      document.getElementById("successMessage").classList.remove("d-none");
                   } else if (request.status === 400 || request.status ===500) {
                      errorMsg.textContent = message;
                   } else {
                      errorMsg.textContent = "Server error!"
                   }
                }
             }, false
         );
      }
   });

})();