let signUpButton,signInButton,container,signupForm,signinForm,
smallScreenSignupButton,smallScreenSigninButton,signUpFeedbackElement;

window.addEventListener("DOMContentLoaded", () => {

    signUpButton = document.getElementById('signUp');
    signInButton = document.getElementById('signIn');

    smallScreenSignupButton = document.getElementById('smallScreenSignup');
    smallScreenSigninButton = document.getElementById('smallScreenSignin');

    container = document.getElementById('container');
    signupForm = document.querySelector('form[name=signupForm]');
    signinForm = document.querySelector('form[name=signinForm]');
    
    signUpFeedbackElement = document.querySelector('#signUpFeedback');

    signUpButton.addEventListener('click',changeToSignUpView);
    signInButton.addEventListener('click',changeToSignInView );

    smallScreenSignupButton.addEventListener('click',changeToSignUpView);
    smallScreenSigninButton.addEventListener('click',changeToSignInView);

    smallScreenSignupButton.addEventListener('click',flipViews);
    smallScreenSigninButton.addEventListener('click',flipViews)

    signupForm.addEventListener('submit', onSignUpFormSubmit);
    signinForm.addEventListener('submit', onSignInFormSubmit);

});

const changeToSignUpView = () => {
	
	resetForm(signinForm);
	
	container.classList.add("right-panel-active");
}

const changeToSignInView = () => {
	
	resetForm(signupForm);
	
	container.classList.remove("right-panel-active");
}

const flipViews = () => {

	container.classList.remove("flip");

	container.offsetWidth = container.offsetWidth;

	container.classList.add("flip");

}

const onSignUpFormSubmit = (event) => {

    event.preventDefault();

    let isFormValid = checkFormValidity(signupForm);

    if(isFormValid) {

    	let formData = new FormData(signupForm);
    	
    	let data = JSON.stringify(Object.fromEntries(formData.entries()));

    	let request = new Request('/user/register',
	      	{ headers: {'Content-Type': 'application/json'},
	  	    	method: 'POST',
	  	    	body: data
	        }
    	);
    	
    	showLoader();

    	fetch(request).then((response) => {
    		
    		hideLoader();
    		
    		if(response.ok){
    			return response.text();
	      	} else{
	      		
	      		let message;
				
				if(response.status == "409") {
					message = response.headers.get("statusText");
				} else {
					message = "Unable to create account.";
				}
				
				throw new Error(message);
	      	}

    	}).then((message) => {
    		
    		resetForm(signupForm);
    		
    		showSuccessFeedback(signUpFeedbackElement,message);
    		
    	}).catch((error) => {
    		
    		showErrorFeedback(signUpFeedbackElement,error.message);
    		
    	})

    }


}

const onSignInFormSubmit = (event) => {

  let isFormValid = checkFormValidity(signinForm);

  if(!isFormValid)
      event.preventDefault();

  return isFormValid;

}


const resetForm = (form) => {
	
	form.reset();
	
	resetFormValidation(form);
	
	
}

const showSuccessFeedback = (feedbackElement, textContent) => {
	
	feedbackElement.textContent  = textContent;
	
	feedbackElement.classList.remove("feedback-error");
	
	feedbackElement.classList.add("feedback-success");
	
	feedbackElement.classList.remove("hide");
	
}

const showErrorFeedback = (feedbackElement, textContent) => {
	
	feedbackElement.textContent  = textContent;
	
	feedbackElement.classList.remove("feedback-success");
	
	feedbackElement.classList.add("feedback-error");
	
	feedbackElement.classList.remove("hide");
	
}
