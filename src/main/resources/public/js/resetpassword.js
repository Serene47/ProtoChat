
let resetPasswordForm;

window.addEventListener("DOMContentLoaded", () => {
	
	resetPasswordForm = document.querySelector("form[name=resetPasswordForm]");
	
	resetPasswordForm.addEventListener("submit", onFormSubmit);

})


const onFormSubmit = (event) => {
	
	let isFormValid = checkFormValidity(resetPasswordForm);

	if(!isFormValid)
		event.preventDefault();
	
	return isFormValid;
}
