CREATE TABLE IF NOT EXISTS `groovy_script` (
    `id` int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `name` varchar(100) NOT NULL,
    `body` varchar(300)
)ENGINE=InnoDB DEFAULT CHARSET=UTF8;
