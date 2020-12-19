

let forgotPasswordForm,usernameInput,feedbackElement;

window.addEventListener("DOMContentLoaded", () => {
	
	forgotPasswordForm = document.querySelector("form[name=forgotPasswordForm]");
	
	usernameInput = document.getElementById("username");
	
	feedbackElement = document.querySelector(".feedback");

	forgotPasswordForm.addEventListener("submit", onFormSubmit);

})


let onFormSubmit = (event) => {
	
	event.preventDefault();
	
	let isFormValid = checkFormValidity(forgotPasswordForm);

    if(isFormValid) {
    	
    	let username = usernameInput.value;
    	
    	showLoader();
    	
    	fetch("user/resetPasswordUserVerify",{
    		method : "POST",
    		body : username
    	})
    	.then( (response) => {
    		
    		hideLoader();
    		
    		feedbackElement.classList.remove("hide");
    		
    		if(response.ok) {
    			return response.text();
    		} else {
    			throw Error(response.status);	
    		}
    		
    	})
    	.then(handleSucessResponse)
    	.catch(handleErrorResponse);
    }
	
	
	
}

let  handleSucessResponse = (data) => {
	
	feedbackElement.textContent = "A confirmation mail has send to your email";
	
	feedbackElement.classList.remove("feedback-error");
	
	feedbackElement.classList.add("feedback-success");
	
}

let  handleErrorResponse = (error) => {
	
	if(error.message  == 401) {
		feedbackElement.textContent = "No user is registered with given email";
	} else {
		feedbackElement.textContent = "Something went wrong";
	}
	
	feedbackElement.classList.remove("feedback-success");
	
	feedbackElement.classList.add("feedback-error");
	
	
}




