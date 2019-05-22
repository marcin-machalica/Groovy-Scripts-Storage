UPDATE `groovy_script` SET `body` = '' WHERE `body` IS NULL;

ALTER TABLE `groovy_script` MODIFY `body` NOT NULL;
