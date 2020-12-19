
DROP TABLE IF EXISTS `ChatRoomMembers` $$

CREATE TABLE `ChatRoomMembers` (
  `chatRoomMember_Id` int NOT NULL AUTO_INCREMENT,
  `chatRoom_Id` int NOT NULL,
  `user_Id` int NOT NULL,
  `unreadMessages` smallint NOT NULL,
  PRIMARY KEY (`chatRoomMember_Id`),
  UNIQUE KEY `chatRoomMember_Id_UNIQUE` (`chatRoomMember_Id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci $$

DROP TABLE IF EXISTS `ChatRooms`$$

CREATE TABLE `ChatRooms` (
  `chatRoom_Id` int NOT NULL AUTO_INCREMENT,
  `lastMessage` varchar(2000) NOT NULL,
  `lastMessageTimestamp` bigint NOT NULL,
  PRIMARY KEY (`chatRoom_Id`),
  UNIQUE KEY `chatRoom_Id_UNIQUE` (`chatRoom_Id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci $$

DROP TABLE IF EXISTS `Messages` $$

CREATE TABLE `Messages` (
  `message_Id` int NOT NULL AUTO_INCREMENT,
  `content` varchar(2000) NOT NULL,
  `chatRoom_Id` int NOT NULL,
  `fromUser_Id` int NOT NULL,
  `toUser_Id` int NOT NULL,
  `timestamp` bigint NOT NULL,
  `readStatus` bit(1) NOT NULL DEFAULT b'0',
  PRIMARY KEY (`message_Id`),
  UNIQUE KEY `message_id_UNIQUE` (`message_Id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci $$

DROP TABLE IF EXISTS `Users` $$

CREATE TABLE `Users` (
  `user_Id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` varchar(100) NOT NULL,
  `enabled` bit(1) NOT NULL DEFAULT b'1',
  `displayName` varchar(50) NOT NULL,
  `displayPicture` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`user_Id`),
  UNIQUE KEY `User_Id_UNIQUE` (`user_Id`),
  UNIQUE KEY `username_UNIQUE` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci $$

DROP TABLE IF EXISTS `UserVerificationTokens` $$

CREATE TABLE `UserVerificationTokens` (
  `userVerificationToken_Id` int NOT NULL AUTO_INCREMENT,
  `user_Id` int NOT NULL,
  `token` varchar(50) NOT NULL,
  `expiry` bigint NOT NULL,
  `type` varchar(50) NOT NULL,
  PRIMARY KEY (`userVerificationToken_Id`),
  UNIQUE KEY `userVerificationToken_Id` (`userVerificationToken_Id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci $$

DROP PROCEDURE IF EXISTS `getMessages` $$

CREATE PROCEDURE `getMessages`(
	IN chatRoom_Id INT ,
    IN user_Id INT
)
BEGIN

	UPDATE Messages
    SET readStatus = 1
    WHERE Messages.chatRoom_Id = chatRoom_Id
		AND Messages.touser_Id = user_Id;
        
	UPDATE ChatRoomMembers
    SET unreadMessages = 0 
    WHERE ChatRoomMembers.chatRoom_Id = chatRoom_Id
		AND ChatRoomMembers.user_Id = user_Id;
    
    SELECT message_Id,Messages.chatRoom_Id,fromUser_Id,toUser_Id,
			content,timestamp,readStatus
    FROM Messages
    WHERE Messages.chatRoom_Id = chatRoom_Id
	ORDER BY timestamp;

END $$

DROP PROCEDURE IF EXISTS `getRecentContacts` $$

CREATE  PROCEDURE `getRecentContacts`(
	IN user_Id INT
)
BEGIN

	WITH 
		ChatRoomsForUser AS 
        (SELECT ChatRoomMembers.chatRoom_Id FROM ChatRooms
        INNER JOIN ChatRoomMembers ON ChatRoomMembers.chatRoom_Id = ChatRooms.chatRoom_Id
        WHERE ChatRoomMembers.user_Id = user_Id)
	SELECT ChatRooms.chatRoom_Id, -- ChatRooms.lastMessage,ChatRooms.lastMessageTimestamp,
    Users.user_Id, Users.displayName, Users.displayPicture,  MessagesCount.unreadMessages as 'inCommingUnread'
    FROM ChatRooms 
    INNER JOIN ChatRoomMembers as Contacts ON Contacts.chatRoom_Id = ChatRooms.chatRoom_Id 
				AND Contacts.user_Id != user_Id
	INNER JOIN ChatRoomMembers as MessagesCount ON Contacts.chatRoom_Id = MessagesCount.chatRoom_Id 
				AND MessagesCount.user_Id = user_Id
    INNER JOIN Users ON Users.user_Id = Contacts.user_Id
    WHERE ChatRooms.chatRoom_Id IN ( SELECT ChatRoomsForUser.chatRoom_Id FROM ChatRoomsForUser);

END $$

DROP PROCEDURE IF EXISTS `markMessagesRead` $$

CREATE PROCEDURE `markMessagesRead`(
	IN messageIdList VARCHAR(2000),
    IN user_Id INT
)
BEGIN

	DECLARE chatRoom_Id INT;
	DECLARE unreadMessageCount INT;

	UPDATE Messages
    SET readStatus = 1
    WHERE FIND_IN_SET(message_Id,messageIdList);
    
    SELECT Messages.chatRoom_Id 
    FROM Messages
	WHERE FIND_IN_SET(message_Id,messageIdList)
    GROUP BY Messages.chatRoom_Id
    INTO chatRoom_Id;

	SELECT COUNT(Messages.message_Id)
	FROM Messages
	WHERE Messages.toUser_Id = user_Id 
	AND Messages.chatRoom_Id = chatRoom_Id
    AND Messages.readStatus = 0
    INTO unreadMessageCount;

	UPDATE ChatRoomMembers 
    SET unreadMessages = unreadMessageCount
    WHERE ChatRoomMembers.user_Id = user_Id 
    and ChatRoomMembers.chatRoom_Id = chatRoom_Id;
    

END $$

DROP PROCEDURE IF EXISTS `saveMessage` $$

CREATE PROCEDURE `saveMessage`(
	IN fromUser_Id  INT,
    IN toUser_Id INT,
    IN content VARCHAR(2000),
    IN timestamp BIGINT,
    OUT message_Id INT,
    OUT chatRoom_Id INT 
)
BEGIN

	SELECT ChatRooms.chatRoom_Id FROM ChatRooms 
    INNER JOIN ChatRoomMembers ON ChatRoomMembers.chatRoom_Id = ChatRooms.chatRoom_Id
	WHERE ChatRoomMembers.user_Id IN( fromUser_Id,toUser_Id) 
    GROUP BY ChatRooms.chatRoom_Id HAVING COUNT(ChatRoomMembers.chatRoomMember_Id) = 2 
    INTO chatRoom_Id;
    
    IF chatRoom_Id IS NOT NULL 
    THEN
    
		INSERT INTO Messages(content,chatRoom_Id,fromUser_Id,toUser_Id,timestamp,readStatus)
        VALUES (content,chatRoom_Id,fromUser_Id,toUser_Id,timestamp,0);
        
        SET message_Id = LAST_INSERT_ID();
        
        UPDATE ChatRooms 
        SET 
			lastMessage = content,
            lastMessageTimestamp = timestamp
		WHERE ChatRooms.chatRoom_Id = chatRoom_Id;
        
        UPDATE ChatRoomMembers
			SET unreadMessages = unreadMessages + 1
		WHERE ChatRoomMembers.chatRoom_Id = chatRoom_Id
        AND ChatRoomMembers.user_Id = toUser_Id;
    
    ELSE 
		
        INSERT INTO ChatRooms(lastMessage,lastMessageTimestamp)
        VALUES (content,timestamp);
        
        SET chatRoom_Id = LAST_INSERT_ID();
        
        INSERT INTO ChatRoomMembers(chatRoom_Id,user_Id,unreadMessages)
        VALUES(chatRoom_Id,fromUser_Id,0);
        
        INSERT INTO ChatRoomMembers(chatRoom_Id,user_Id,unreadMessages)
        VALUES(chatRoom_Id,toUser_Id,1);
        
        INSERT INTO Messages(content,chatRoom_Id,fromUser_Id,toUser_Id,timestamp,readStatus)
        VALUES (content,chatRoom_Id,fromUser_Id,toUser_Id,timestamp,0);
        
        SET message_Id = LAST_INSERT_ID();
    
    
    END IF;
    
    

END $$


