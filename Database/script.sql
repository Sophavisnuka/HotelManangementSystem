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

create table reservation (
    reservationID int auto_increment,
    userId int auto_increment,
    checkInDate date not null,
    checkOutDate date not null,
    durationOfStay int,
    roomNumbers int,
    primary key (userId, reservationID),
);