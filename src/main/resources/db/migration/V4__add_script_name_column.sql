ALTER TABLE `groovy_script` ALTER COLUMN `name` RENAME TO `method_name`;

ALTER TABLE `groovy_script` ADD `script_name` varchar(100) UNIQUE;

UPDATE `groovy_script` SET `script_name` = `id` WHERE `script_name` IS NULL;

ALTER TABLE `groovy_script` MODIFY `script_name` NOT NULL;
