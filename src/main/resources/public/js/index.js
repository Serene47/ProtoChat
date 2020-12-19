
let socket;

let messageLog,recentContactList,allContactList,messageForm,contactDisplayPictureElement,contactNameElement,displayNameElement,displayPictureElement,
	backButton,messageWrapper,noContactElement,contactSelectedElement,toggleContacts,contactsTitle,searchContactsInpu,userProfileButton,searchContactsElement,
	messaeInput;
	


let user_Id,contactName,contactUser_Id,contactDisplayPicture,lastMessageDate,readMessages =[],contactsView = "recentContacts",chatRoom_Id;


let initializeElements = () => {

    messageLog = document.querySelector("#messageLog");
    
    userProfileButton = document.querySelector("#userProfileButton");

    displayNameElement = document.querySelector("#displayName");
    
    displayPictureElement = document.querySelector("#displayPicture");

    recentContactList = document.querySelector("#recentContactList");
    
    allContactList = document.querySelector("#allContactList");

    messageForm = document.querySelector("form[name=messageForm]");
    
    messaeInput = messageForm.querySelector("input[name=message]");

    contactNameElement = document.querySelector("#contactName");
    
    contactDisplayPictureElement = document.querySelector("#contactDisplayPicture");

    backButton = document.querySelector("#backButton");

    messageWrapper = document.querySelector(".messages-wrapper");
    
    noContactElement = document.querySelector(".no-contact");
    
    contactSelectedElement = document.querySelector(".contact-selected");
    
    toggleContacts = document.querySelector("#toggleContacts");
    
    contactsTitle  = document.querySelector("#contactsTitle");
    
    searchContactsInput = document.querySelector("#searchContacts"); 
    
    for(let contactElement of recentContactList.children) {
    	
    	contactElement.addEventListener("click",onContactSelected );
    	
    }

    for(let contactElement of allContactList.children) {
    	
    	contactElement.addEventListener("click",onContactSelected );
    	
    }
    
    userProfileButton.addEventListener("click",showProfileMenu);
    
    messageForm.addEventListener("submit",sendMessage );
    
    backButton.addEventListener("click",onBackButtonPressed);
    
    toggleContacts.addEventListener("click",changeContactsView);
    
    searchContactsInput.addEventListener("input", searchContacts);
    
    contactDisplayPictureElement.addEventListener("click",toggleContactPicture);
    
    
}

let initializeWebSocket  = () => {

    socket = new WebSocket("ws://localhost:8080/chat")

    socket.onopen = onOpen;
    socket.onmessage = onMessage;
    socket.onclose = onClose;
    socket.onerror = onError;

}

let onMessage = (event) => {
	
	console.log(event)

    let message =  JSON.parse(event.data);

    if(message.fromUser_Id == contactUser_Id) {

        addMessageToLog(message,"recieved",true);
        
        readMessages.push(message.messageId);
        
        if(!chatRoom_Id) {
        	
        	chatRoom_Id = message.chatRoom_Id;
        	
        	addToRecent(message.fromUser_Id,chatRoom_Id);
        	
        }
        
    } else if(message.fromUser_Id == user_Id) {
    	
    	if(!chatRoom_Id) {
        	
    		chatRoom_Id = message.chatRoom_Id;
    		
        	addToRecent(message.toUser_Id,chatRoom_Id);
        	
        }
    	
    } else {
    	
    	let contactElement = recentContactList.querySelector("li[data-user-id=\""+ message.fromUser_Id +"\"]");
    	
    	if(!contactElement){
    		
    		contactElement = addToRecent(message.fromUser_Id,message.chatRoom_Id);
    		
    	}
    	
    	modifyActiveMessageCount(contactElement,"increment")
    	
    }

}

let onClose = (event) => {

    console.log("Socket Clossed");

}

let onOpen = (event) => {

    console.log("Socket Opened");
}

let onError = (event) => {

    console.log("Error Occoured");

    console.log(event);
}

let sendMessage = (event) => {

    event.preventDefault();

    if(messaeInput.value){
    	
    	let message = {
	        fromUser_Id : user_Id,
	        toUser_Id : contactUser_Id,
	        content : messaeInput.value,
	        timestamp : new Date().getTime()
	    }

	    socket.send(JSON.stringify(message));
    	
    	addMessageToLog(message,"send",true);
    }
    
    messaeInput.value =  "";

}

let addToRecent = (user_Id,chatRoom_Id) => {
	
	let contactElement = allContactList.querySelector("li[data-user-id=\""+ user_Id +"\"]");
	
	let recentContact = contactElement.cloneNode(true);
	
	let unreadMessagesSpan = document.createElement("span");
	
	unreadMessagesSpan.classList.add("hide");
	
	unreadMessagesSpan.classList.add("active-messages");
	
	recentContact.setAttribute("data-chat-room-id",chatRoom_Id);
    
	recentContact.appendChild(unreadMessagesSpan);
	
	recentContact.addEventListener("click",onContactSelected)

    recentContactList.appendChild(recentContact);
	
	return recentContact;
	
}



let addMessageToLog = (message,direction,isLast) => {

    let messageElement = document.createElement("p");

    messageElement.textContent = message.content;

    messageElement.classList.add(direction);
    
    let timeElement = document.createElement("span");
    
    let date = new Date(message.timestamp);
    
    timeElement.textContent = date.toLocaleTimeString("en-Us",{ hour: '2-digit', minute: '2-digit' });
    
    messageElement.appendChild(timeElement);
    
    date = date.toLocaleDateString().replace(/\//g,"-");;
    
    if(lastMessageDate != date){
    	
    	let dateElement = document.createElement("span");
    	
    	dateElement.textContent = date;
    	
    	dateElement.classList.add("date");
    	
    	messageLog.appendChild(dateElement)
    }

    messageLog.appendChild(messageElement);
        
    lastMessageDate = date;
    
    if(isLast){
    	messageElement.scrollIntoView();
    }

}

let emptyMessageLog = () => {

    while(messageLog.firstChild){
        messageLog.removeChild(messageLog.firstChild)
    }

}

/*

let addContactToList = (contact) => {

    var contactElement = document.createElement("li");
    
    var contactNameSpan = document.createElement("span");
    
    contactNameSpan.textContent = contact.displayName;
    
    contactElement.appendChild(contactNameSpan);
    	
    var unreadMessagesSpan = document.createElement("span");
    
    if(contact.unreadMessagesCount > 0){
        
    	unreadMessagesSpan.textContent = contact.unreadMessagesCount;
    	
    } else {
    	
    	unreadMessagesSpan.classList.add("hide");
    }
    	
    unreadMessagesSpan.classList.add("active-messages");
        
    contactElement.appendChild(unreadMessagesSpan);
    
    contactElement.setAttribute("user_Id",contact.user_Id)

    contactElement.addEventListener("click",onContactSelected)

    contactList.appendChild(contactElement);

}*/

/*
let fetchContacts = () => {

    fetch("/contacts")contact.getInCommingUnread()
    .then((data) => data.json())
    .then((data) => {

        data.forEach( (contact) => {
            addContactToList(contact)
        })

    })
}*/

let fetchPreviousMessages = () => {

	showLoader();
	
    fetch("/messages/" + chatRoom_Id)
    .then((data) => data.json())
    .then((data) => {

    	hideLoader();
    	
    	lastMessageDate = null;
    	
        let direction,isLast,length = data.length;
     
    	data.forEach( (message,index) => {

            direction = message.fromUser_Id == user_Id ? "send" : "recieved";

            isLast = length -1 == index;
            
            addMessageToLog(message,direction,isLast);
            
        })
            
        
    })

}

let fetchUserDetails = () => {
	
	user_Id = document.body.getAttribute("data-user-id");
}

let onContactSelected = (event) => {

	emptyMessageLog();
	
	if(contactUser_Id ){
		markMessagesRead();
	}
	
    let target = event.currentTarget;

    contactName = target.getAttribute("data-display-name");
    
    contactUser_Id  = target.getAttribute("data-user-id");
    
    chatRoom_Id = target.getAttribute("data-chat-room-id");
    
    if(chatRoom_Id) {
    	
    	fetchPreviousMessages();
    	
    } else {
    	
    	let recentContactElement = recentContactList.querySelector("li[data-user-id=\""+ contactUser_Id +"\"]");
    	
    	if(recentContactElement) {
    		
    		chatRoom_Id = recentContactElement.dataset.chatRoomId;
    		
    		fetchPreviousMessages();

    	}
    	
    }
    
    contactDisplayPicture = target.getAttribute("data-display-picture");

    contactNameElement.textContent = contactName;
    
    contactDisplayPictureElement.setAttribute("src", contactDisplayPicture);
	
	modifyActiveMessageCount(target,"clear");
    	
    messageWrapper.classList.add("active");
    
    noContactElement.classList.add("hide");
    
    contactSelectedElement.classList.remove("hide");
    
    readMessages =[];

}

let onBackButtonPressed = (event) => {

    contactName = null;
    
    contactUser_Id =null;
    
    chatRoom_Id = null;
    
    contactDisplayPicture =null;

    messageWrapper.classList.remove("active");
    
    noContactElement.classList.remove("hide");
    
    contactSelectedElement.classList.add("hide");

    markMessagesRead();
}

let markMessagesRead = () => {
	
	if(!navigator.sendBeacon) return;
	
	if(readMessages.length > 0){
		
		let url = "/messages/markMessagesRead" ;
		
		let data = readMessages.join(",");
		
		// Send the beacon
		var status = navigator.sendBeacon(url,data);
	}
	
}



let modifyActiveMessageCount = (contactElement,operation) => {
	
	var unreadMessagesSpan = contactElement.querySelector(".active-messages");
	
	if(unreadMessagesSpan){
		
		 if(operation == "increment"){
		     
			 var unreadMessagesCount = unreadMessagesSpan.textContent? parseInt(unreadMessagesSpan.textContent) : 0;
			 
			 unreadMessagesSpan.textContent = unreadMessagesCount + 1;
			 
			 unreadMessagesSpan.classList.remove("hide");
			 
		 } else if(operation == "clear") {
			 
			 unreadMessagesSpan.textContent = "";
			 
			 unreadMessagesSpan.classList.add("hide");
		 }
	}	
}

let changeContactsView = () => {
	
	if(contactsView == "recentContacts"){
		
		recentContactList.classList.add("hide");
		
		allContactList.classList.remove("hide");
		
		toggleContacts.textContent = "Recent Contacts";
		
		contactsView = "allContacts";
		
		contactsTitle.textContent = "All Users";
		
	} else {
		
		allContactList.classList.add("hide");
		
		recentContactList.classList.remove("hide");
		
		toggleContacts.textContent = "All Users";
		
		contactsView = "recentContacts";
		
		contactsTitle.textContent = "Recent Contacts";
		
	}
	
	searchContactsInput.value = "";
	
	searchContacts();
	
}

let searchContacts = (event) => {
	
	let searchText = searchContactsInput.value;
	
	let contact,contactName,contactList;
	
	contactList = contactsView == "recentContacts" ?  recentContactList : allContactList ;
		
	for( contact of contactList.children  ) {
		
		if(contact.dataset.displayName.includes(searchText)) {
			
			contact.classList.remove("hide");
			
		} else {
			
			contact.classList.add("hide");
			
		}
		
	}
		
}

let toggleContactPicture = () => {
	
	let isEnlarged = contactDisplayPictureElement.getAttribute("data-enlarged") == "true";
	
	contactDisplayPictureElement.setAttribute("data-enlarged", !isEnlarged);
	
}

let showProfileMenu = () => {
	
	let dropdownMenu = userProfileButton.nextElementSibling;
	
	dropdownMenu.classList.remove("hide");
	
}


document.addEventListener("click", (event) => {
	
	let target = event.target;
	
	if(target != contactDisplayPictureElement) {
		
		contactDisplayPictureElement.setAttribute("data-enlarged",false)
		
	}
	
	if(!userProfileButton.contains(target)) {
		
		let dropdownMenu = userProfileButton.nextElementSibling;
		
		dropdownMenu.classList.add("hide");

	}
	
	
});

let resetForm = (form) => {
	
	form.reset();
	
	resetFormValidation(form);
	
}

let showSuccessFeedback = (feedbackElement, feedbackTimeout,textContent) => {
	
	feedbackElement.textContent = textContent;
	
	feedbackElement.classList.remove("feedback-error");
	
	feedbackElement.classList.add("feedback-success");
	
	feedbackElement.classList.remove("hide");
	
	clearTimeout(feedbackTimeout);
	
	feedbackTimeout = setTimeout ( () => {
		
		feedbackElement.classList.add("hide");
		
	},3000);
}

let showErrorFeedback = (feedbackElement,feedbackTimeout, textContent) => {
	
	feedbackElement.textContent = textContent;
	
	feedbackElement.classList.remove("feedback-success");
	
	feedbackElement.classList.add("feedback-error");
	
	feedbackElement.classList.remove("hide");
	
	clearTimeout(feedbackTimeout);
	
	feedbackTimeout = setTimeout ( () => {
		
		feedbackElement.classList.add("hide");
		
	},3000);
}

window.addEventListener("DOMContentLoaded",() => {

    initializeElements();

    initializeWebSocket();

    fetchUserDetails();
    
    initializeProfileModalElements();
    
    initializePasswordModalElements();

})

window.onclose = () => {

    socket.close();
}

window.onunload = markMessagesRead;






