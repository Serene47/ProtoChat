
let updateProfileModal,updateProfileButton,closeProfileModalButton,updateDisplayPictureForm,updateDisplayNameForm ,
	displayNameFeedbackElement, displayPictureFeedbackElement, displayPictureFeedbackTimeout, displayNameFeedbackTimeout, 
	changeImageButton, removeImageButton, previewImage;
	


let initializeProfileModalElements = () => {
	
	updateProfileModal = document.querySelector("#updateProfileModal");
	
	updateProfileButton =  document.querySelector("#updateProfileButton");
    
    closeProfileModalButton = document.querySelector("#closeProfileModal");
    
    updateDisplayPictureForm = document.querySelector("form[name=updateDisplayPictureForm]");
	
	updateDisplayNameForm = document.querySelector("form[name=updateDisplayNameForm]");
	
	displayNameFeedbackElement =  document.querySelector("#displayNameFeedback");
	
	displayPictureFeedbackElement =  document.querySelector("#displayPictureFeedback");
    
    changeImageButton = document.querySelector("#changeImageButton");
    
    removeImageButton = document.querySelector("#removeImageButton");
	
	previewImage = document.querySelector("#previewImage");
	
	displayPictureInput = document.querySelector("#displayPic");
    
	closeProfileModalButton.addEventListener("click",closeProfileModal);
	
	updateProfileButton.addEventListener("click",openProfileModal);
	
	updateDisplayNameForm.addEventListener("submit",updateDisplayName); 
    
    changeImageButton.addEventListener("click",changeImage);
    
    removeImageButton.addEventListener("click",removeImage);
    
    displayPictureInput.addEventListener("change",displayPicOnChange);
	
}

let openProfileModal = () =>{
	
	updateProfileModal.classList.remove("hide")
}

let closeProfileModal = () =>{
	
	updateProfileModal.classList.add("hide");
	
}

let changeImage = () => {
	
	displayPictureInput.value = "";
	
	displayPictureInput.click();
	
}


let displayPicOnChange = (event) => {
	
	let target = event.target;
	
	let displayPictureFile = target.files[0];
	
	//document.querySelector("#previewImage").setAttribute("src",URL.createObjectURL(file));
	
	if(validteFile(target)) {
		
		let formData = new FormData();
		
		formData.append("image",displayPictureFile);
		
		showLoader();
		
		fetch("user/updateDisplayPicture",{
			method : "POST",
			body : formData
		})
		.then ((response) => { 
		
			hideLoader();
			
			if(response.ok){
				
				return response.json();
				
			} else {
				
				throw new Error(response.statusText);
				
			}
		})
		.then((data) => {
			
			previewImage.setAttribute("src",data.url);
			
			let thumbnailUrl = data.url.replace("display-pictures/" + user_Id , "display-pictures/" + user_Id + "/thumbnails");
			
			displayPictureElement.setAttribute("src",thumbnailUrl);
			
			removeImageButton.classList.remove("hide");
			
			showSuccessFeedback(displayPictureFeedbackElement, displayPictureFeedbackTimeout ,"Display Picture updated");
			
		})
		.catch((error) => {
			
			showErrorFeedback(displayPictureFeedbackElement, displayPictureFeedbackTimeout ,"Display Picture updation failed");
			
		})
		
	}
	
	
	
}


let removeImage = () => {
	
	showLoader();
	
	fetch("user/removeDisplayPicture",{
		method : "DELETE"
	})
	.then ((response) => { 
		
		hideLoader();
		
		if(response.ok){
			
			return response.text();
			
		} else {
			
			throw new Error(response.statusText);
			
		}
	})
	.then((data) => {
		
		previewImage.setAttribute("src","display-pictures/default/default-user.png");	
		
		displayPictureElement.setAttribute("src","display-pictures/default/thumbnails/default-user.png");	
		
		removeImageButton.classList.add("hide");
		
		showSuccessFeedback(displayPictureFeedbackElement, displayPictureFeedbackTimeout ,"Display Picture removed");
	})
	.catch((error) => {
		
		showErrorFeedback(displayPictureFeedbackElement, displayPictureFeedbackTimeout ,"Display Picture updation failed");
		
	})
	
}

let updateDisplayName = (event) => {
	
	event.preventDefault();
	
	let isFormValid = checkFormValidity(updateDisplayNameForm);
	
	if(isFormValid) {
		
		let displayName = updateDisplayNameForm.querySelector("input[name=displayName]").value;
		
		showLoader();
		
		fetch("user/updateDisplayName",{
			method : "PATCH",
			body : displayName
		})
		.then((response) => { 
			
			hideLoader();
			
			if(response.ok){
				
				return response.text();
				
			} else {
				
				throw new Error(response.statusText);
				
			}
		})
		.then((data) => {
			
			displayNameElement.textContent = displayName;
			
			showSuccessFeedback(displayNameFeedbackElement, displayNameFeedbackTimeout ,"Display Name updated");
				
		})
		.catch((error) => {
			
			showErrorFeedback(displayNameFeedbackElement, displayNameFeedbackTimeout ,"Display Name updation failed");
			
		});
	}
	
}





