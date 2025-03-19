-- create database hotelManagement;
-- use hotelManagement;


create table users (
    userId INT auto_increment PRIMARY KEY,
    userName varchar(100) not null,
    phoneNumber varchar(20) not null,
    email varchar (100) not null,
    passwords varchar (100) not null
    foreign key (userId) references reservation (userId)
);  

create table room (
    roomId int primary key,
    roomType varchar (100),
    roomPrice decimal(10,2),
    roomStatus (10)
    reservationID int
    foreign key (reservationID) references reservation (reservationID)
);

CREATE TABLE reservation (
    reservationId VARCHAR(10) PRIMARY KEY,
    roomId INT,
    checkInDate DATE,
    checkOutDate DATE,
    durationOfStay INT,
    roomType VARCHAR(50),
    FOREIGN KEY (roomId) REFERENCES room(roomId)
);