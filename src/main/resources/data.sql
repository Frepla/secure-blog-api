CREATE TABLE authors (
                         id INT PRIMARY KEY AUTO_INCREMENT,
                         username VARCHAR(50) NOT NULL UNIQUE,
                         email VARCHAR(100)
);

CREATE TABLE posts (
                       id INT PRIMARY KEY AUTO_INCREMENT,
                       title VARCHAR(255) NOT NULL,
                       content TEXT NOT NULL,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       author_id INT,
                       FOREIGN KEY (author_id) REFERENCES authors(id)
);

INSERT INTO authors (username, email) VALUES ('user1', 'user1@example.com');
INSERT INTO authors (username, email) VALUES ('user2', 'user2@example.com');
INSERT INTO authors (username, email) VALUES ('admin', 'admin@example.com');

INSERT INTO posts (title, content, author_id, updated_at) VALUES
                                                              ('User1:s första inlägg', 'Detta är user1:s första blogginlägg.', 1, CURRENT_TIMESTAMP),
                                                              ('User2:s första inlägg', 'Detta är user2:s första blogginlägg.', 2, CURRENT_TIMESTAMP),
                                                              ('Admin inlägg', 'Admin:s inlägg.', 3, CURRENT_TIMESTAMP);