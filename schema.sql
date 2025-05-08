CREATE TABLE IF NOT EXISTS id_sequences (
    tablename VARCHAR(30) PRIMARY KEY,
    next_id INT NOT NULL
);

INSERT IGNORE INTO id_sequences VALUES
('notifications',0),
('likes',0),
('follows',0);

CREATE TABLE IF NOT EXISTS users (
    username VARCHAR (30) PRIMARY KEY,
    bio VARCHAR (128),
    followers_count INT,
    following_count INT,
    password VARCHAR (128),
    image VARCHAR (128),
    post_count INT
);

CREATE TABLE IF NOT EXISTS posts (
    image VARCHAR (128),
    caption VARCHAR (128),
    timestamp TIMESTAMP,
    likeCount INT,
    username VARCHAR (30),
    post_id CHAR(16) PRIMARY KEY,
    FOREIGN KEY (username) REFERENCES users(username)
);

CREATE TABLE IF NOT EXISTS notifications (
    sender_username VARCHAR (30),
    receive_username VARCHAR (30),
    post_id CHAR(16),
    type VARCHAR (16),
    timestamp TIMESTAMP,
    notification_id CHAR (16) PRIMARY KEY,
    FOREIGN KEY (post_id) REFERENCES posts(post_id),
    FOREIGN KEY (sender_username) REFERENCES users(username),
    FOREIGN KEY (receive_username) REFERENCES users(username)
);

DELIMITER //
CREATE TRIGGER before_notification_insert
    BEFORE INSERT ON notifications
    FOR EACH ROW
BEGIN
    DECLARE next_num INT;
    UPDATE id_sequences SET next_id = LAST_INSERT_ID(next_id + 1) WHERE tablename = 'notifications';
    SET next_num = LAST_INSERT_ID();
    SET NEW.notification_id = LPAD(next_num, 16, '0');
END//
DELIMITER ;

CREATE TABLE IF NOT EXISTS likes (
    like_id CHAR (16) PRIMARY KEY,
    username VARCHAR (30),
    post_id CHAR (16),
    notification_id CHAR (16),
    FOREIGN KEY (username) REFERENCES users(username),
    FOREIGN KEY (post_id) REFERENCES posts(post_id),
    FOREIGN KEY (notification_id) REFERENCES notifications(notification_id)
);

DELIMITER //
CREATE TRIGGER before_like_insert
    BEFORE INSERT ON likes
    FOR EACH ROW
BEGIN
    DECLARE next_num INT;
    UPDATE id_sequences SET next_id = LAST_INSERT_ID(next_id + 1) WHERE tablename = 'likes';
    SET next_num = LAST_INSERT_ID();
    SET NEW.like_id = LPAD(next_num, 16, '0');
END//
DELIMITER ;

CREATE TABLE IF NOT EXISTS follows (
    username VARCHAR (30),
    user_followed VARCHAR (30),
    follow_id CHAR (16),
    FOREIGN KEY (username) REFERENCES users(username),
    FOREIGN KEY (user_followed) REFERENCES users(username)
);

DELIMITER //
CREATE TRIGGER before_follows_insert
    BEFORE INSERT ON follows
    FOR EACH ROW
BEGIN
    DECLARE next_num INT;
    UPDATE id_sequences SET next_id = LAST_INSERT_ID(next_id + 1) WHERE tablename = 'follows';
    SET next_num = LAST_INSERT_ID();
    SET NEW.follow_id = LPAD(next_num, 16, '0');
END//
DELIMITER ;

INSERT INTO users (username, bio, followers_count, following_count, password, image, post_count) VALUES
('Zara','Humanoid robot much like the rest',2,1, 'Password','image/storage/profile/Zara.png',2),
('Xylo','Fierce warrior, not solo',1,1, 'Password','image/storage/profile/Xylo.png', 2),
('Mystar','Xylo and I are not the same!',1,2, 'Password','image/storage/profile/Mystar.png',2),
('Minh','I am Minh',0,0, 'Password','image/storage/profile/Minh.png',0),
('Lorin','For copyright reasons, I am not Grogu',3,3, 'Password','image/storage/profile/Lorin.png',3);


INSERT INTO posts (image,caption,likeCount,timestamp,username,post_id) VALUES
('img/uploaded/Xylo_1.png','My tea strong as Force is.',0,'2023-12-17 19:22:40','Xylo','Xylo_1'),
('img/uploaded/Xylo_2.png','Jedi mind trick failed.',0,'2023-12-17 19:23:14','Xylo', 'Xylo_2'),
('img/uploaded/Zara_1.png','Lost my map I have. Oops.',0,'2023-12-17 19:24:31','Zara', 'Zara_1'),
('img/uploaded/Zara_2.png','Yoga with Yoda',0,'2023-12-17 19:25:03','Zara', 'Zara_2'),
('img/uploaded/Mystar_1.png','Yoga with Yoda',0,'2023-12-17 19:26:50','Mystar', 'Mystar_1'),
('img/uploaded/Mystar_2.png','In my soup a fly is.',0,'2023-12-17 19:27:24','Mystar', 'Mystar_2'),
('img/uploaded/Lorin_1.png','Meditate I must.',0,'2023-12-17 19:09:35','Lorin', 'Lorin_1'),
('img/uploaded/Lorin_2.png','In the cookie jar my hand was not.',0,'2023-12-17 19:07:43','Lorin', 'Lorin_2'),
('img/uploaded/Lorin_3.png','Caption',0,'2025-02-23 21:43:45','Lorin', 'Lorin_3');

INSERT INTO notifications (sender_username,receive_username,type,timestamp,post_id) VALUES
('Lorin','Zara','like','2023-12-17 19:29:41','Lorin_1'),
('Zara','Lorin','like','2025-02-19 14:38:40', 'Zara_1'),
('Zara','Lorin','like','2025-02-19 14:38:50', 'Zara_1');

INSERT INTO follows (username, user_followed) VALUES
('Xylo','Lorin'),
('Zara','Lorin'),
('Mystar','Lorin'),
('Mystar','Zara'),
('Lorin','Mystar'),
('Lorin','Zara'),
('Lorin','Xylo');