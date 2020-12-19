

const showLoader = () => {
	
	removePreviousloaders();
	
	let loaderContainer = document.createElement("div");
	
	loaderContainer.classList.add("loader-container");
	
	loaderContainer.innerHTML = "<div class='lds-facebook'><div></div><div></div><div></div></div>";
	
	document.body.appendChild(loaderContainer);
	
}

const removePreviousloaders = () => {
	
	let loaders = document.querySelectorAll(".loader-container");
	
	for(loader of loaders) {
		document.body.removeChild(loader);
	}
}

const hideLoader = () => {
	
	let loaderContainer = document.querySelector(".loader-container");
	
	document.body.removeChild(loaderContainer);
}