create database hotelSystem;

use hotelSystem;

-- Fix: Create room table first
CREATE TABLE room (
    roomId INT PRIMARY KEY,
    roomType VARCHAR(100),
    roomPrice DECIMAL(10,2),
    roomStatus VARCHAR(10)
);


-- Fix: Create users table with correct foreign key usage
CREATE TABLE users (
    userId INT AUTO_INCREMENT PRIMARY KEY,
    userName VARCHAR(100) NOT NULL,
    phoneNumber VARCHAR(20) NOT NULL,
    email VARCHAR(100) NOT NULL,
    passwords VARCHAR(100) NOT NULL
);

-- Fix: Add userId to reservation and correct foreign key in room
CREATE TABLE reservation (
    reservationId VARCHAR(10) PRIMARY KEY,
    userId INT,  -- Added userId to track which user made the reservation
    roomId INT,
    checkInDate DATE,
    checkOutDate DATE,
    durationOfStay INT,
    roomType VARCHAR(50),
    FOREIGN KEY (roomId) REFERENCES room(roomId),
    FOREIGN KEY (userId) REFERENCES users(userId)  -- Corrected foreign key reference
);

-- Fix: Add reservationId as foreign key in room if necessary
ALTER TABLE users ADD COLUMN role enum('admin', 'customer') NOT NULL DEFAULT 'customer';
ALTER TABLE room ADD COLUMN reservationId VARCHAR(10);
ALTER TABLE room ADD CONSTRAINT fk_reservation FOREIGN KEY (reservationId) REFERENCES reservation(reservationId);
