CREATE TABLE answers_to_delete
SELECT DISTINCT a1.id FROM `answers` a1 JOIN `answers` a2 ON a1.pdf_answers_id = a2.pdf_answers_id
WHERE a2.field_id = a1.field_id AND a2.id < a1.id;

ALTER TABLE `answers_to_delete`
	ADD INDEX `id_key` (`id`);

DELETE FROM `answers` WHERE id IN ( SELECT id FROM `answers_to_delete` );

DROP TABLE `answers_to_delete`;