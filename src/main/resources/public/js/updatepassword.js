
let updatePasswordButton,updatePasswordModal,closePasswordModalButton,updatePasswordForm, passwordFeedbackElement,passwordFeedbackTimeout;
	


let initializePasswordModalElements = () => {
	
	updatePasswordButton =  document.querySelector("#updatePasswordButton");
	
	updatePasswordModal = document.querySelector("#updatePasswordModal");
    
    closePasswordModalButton = document.querySelector("#closePasswordModal");

	updatePasswordForm = document.querySelector("form[name=updatePasswordForm]");
	
	passwordFeedbackElement =  document.querySelector("#passwordFeedback");
    
	closePasswordModalButton.addEventListener("click",closePasswordModal);
	
	updatePasswordButton.addEventListener("click",openPasswordModal);
	
	updatePasswordForm.addEventListener("submit",updatePassword); 
	
}

let openPasswordModal = () =>{
	
	updatePasswordModal.classList.remove("hide")
}

let closePasswordModal = () =>{
	
	updatePasswordModal.classList.add("hide");
	
	updatePasswordForm.reset();
}


let updatePassword = (event) => {
	
	event.preventDefault();
	
	let isFormValid = checkFormValidity(updatePasswordForm);
	
	if(isFormValid) {
		
		let formData = new FormData(updatePasswordForm);
		
		showLoader();
		
		fetch("user/updatePassword",{
			method : "PATCH",
			body : formData
		})
		.then((response) => { 
			
			hideLoader();
			
			if(response.ok){
				
				return response.text();
				
			} else {
				
				let message;
				
				if(response.status == "403") {
					message = response.headers.get("statusText");
				} else {
					message = "Unable to update password.";
				}
				
				throw new Error(message);
				
			}
		})
		.then((data) => {
			
			showSuccessFeedback(passwordFeedbackElement,passwordFeedbackTimeout ,"Password updated");
			
			resetForm(updatePasswordForm);
				
		})
		.catch((error) => {
			
			showErrorFeedback(passwordFeedbackElement,passwordFeedbackTimeout,error.message);
			
		});
		
	}
	
}



