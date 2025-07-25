DROP TABLE IF EXISTS reviews;
DROP TABLE IF EXISTS destination_likes;
DROP TABLE IF EXISTS user_roles;
DROP TABLE IF EXISTS destinations;
DROP TABLE IF EXISTS users;

CREATE TABLE IF NOT EXISTS users (
	id INT AUTO_INCREMENT PRIMARY KEY,
	username VARCHAR(255) NOT NULL UNIQUE,
	email VARCHAR(255) NOT NULL UNIQUE,
	password VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS user_roles (
	user_id INT NOT NULL,
	roles VARCHAR(255) NOT NULL,
	PRIMARY KEY (user_id, roles),
	FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS destinations (
	id INT AUTO_INCREMENT PRIMARY KEY,
	title VARCHAR(255) NOT NULL,
	location VARCHAR(255) NOT NULL,
	description VARCHAR(255) NOT NULL,
	image_url VARCHAR(255) NOT NULL,
	user_id INT NOT NULL,
	created_at TIMESTAMP NOT NULL,
	updated_at TIMESTAMP NOT NULL,
	FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS reviews (
	id INT AUTO_INCREMENT PRIMARY KEY,
	rating DOUBLE NOT NULL,
	body VARCHAR(255) NOT NULL,
	user_id INT NOT NULL,
	destination_id INT NOT NULL,
	created_at TIMESTAMP NOT NULL,
	FOREIGN KEY (user_id) REFERENCES users(id),
	FOREIGN KEY (destination_id) REFERENCES destinations(id)
);

CREATE TABLE IF NOT EXISTS destination_likes (
	user_id INT NOT NULL,
	destination_id INT NOT NULL,
	PRIMARY KEY (user_id, destination_id),
	FOREIGN KEY (user_id) REFERENCES users(id),
	FOREIGN KEY (destination_id) REFERENCES destinations(id)
);
