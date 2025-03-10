-- create database hotelManagement;
-- use hotelManagement;


create table users (
    userId INT auto_increment PRIMARY KEY,
    userName varchar(100) not null,
    phoneNumber varchar(20) not null,
    email varchar (100) not null,
    passwords varchar (100) not null
);  

create table reservation (
    reservationId int primary key,
    userName varchar(100) not null,
    userPhoneNumber varchar(20) not null,
    checkIndate date not null,
    checkOutdate date not null,
    durationOfStay int,
    foreign key (useName) references user (UserName);
);