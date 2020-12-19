

let inputControls;

window.addEventListener("DOMContentLoaded",() => {
	
	inputControls = document.querySelectorAll("*[data-validate=true]");
	
	let control,validateOn;
	
	for(control of inputControls) {
		
		control.addEventListener("blur",(event) => {
			checkControlValidity(event.target);
		})
	}

})

const checkFormValidity = (form) => {

    let isFormValid = true, isControlValid;

    for( let formControl of form.elements) {

        if(!["hidden","submit","button","reset" ].includes(formControl.type)) {

           isControlValid = checkControlValidity(formControl);

           isFormValid = isFormValid && isControlValid;

        }
    }

    return isFormValid;
}


const checkControlValidity = (element) => {

    if(!element.validity.valid) {
        showErrorFeeback(element);
    } else {
        removeErrorFeeback(element)
    }

    return element.validity.valid;

}

const resetFormValidation = (form) => {
	
	 for( let formControl of form.elements) {
		 
		  if(!["hidden","submit","button","reset" ].includes(formControl.type)) {
			  
			  removeErrorFeeback(formControl);
			  
		  }
	 }
	
}


const removeErrorFeeback = (element) => {

    let formGroupElement = element.closest(".form-group");

    let errorMessageElement = formGroupElement.querySelector(".field-error-message");

    formGroupElement.classList.remove("error");

    if(errorMessageElement) {
        errorMessageElement.textContent = "";
    }

}


const showErrorFeeback = (element) => {

    let formGroupElement = element.closest(".form-group");

    let errorMessageElement = formGroupElement.querySelector(".field-error-message");

    formGroupElement.classList.add("error");

    if(element.validity.typeMismatch) {

        errorMessageElement.textContent = `Not a valid ${element.type}`;

    } else  if(element.validity.tooShort){

        errorMessageElement.textContent = `Atleast ${element.minLength} characters long`;

    } else  if(element.validity.tooLong){

        errorMessageElement.textContent = `Atmost ${element.maxLength} characters long`;

    } else  if(element.validity.valueMissing){

        errorMessageElement.textContent = "Cannot be empty";
        
    } else  if(element.validity.customError){

    	errorMessageElement.textContent = element.validationMessage;
    
    }
    
}

const validteFile = (element) => {
	
	let maxSize = parseInt(element.dataset.maxSize);
	
	let minSize = parseInt(element.dataset.minSize);
	
	let acceptTypes = element.accept.split(",");
	
	let file = element.files[0];
	
	let fileSize = file.size / 1024;
	
	let fileExtension = "." + file.name.split(".").pop();
	
	if(!acceptTypes.includes(fileExtension)) {
		
		element.setCustomValidity (`File should be ${element.accept} format`);
		
	} else if(fileSize < minSize) {
		
		element.setCustomValidity(`File should be greater than ${minSize} KB`);
		 
	}  else if(fileSize > maxSize) {
		
		element.setCustomValidity(`File should be less than ${maxSize} KB`);
	 
	} else  {
		
		element.setCustomValidity("");
	}
	
	return checkControlValidity(element);
	
	
}

