ALTER TABLE pdf_answers ADD url VARCHAR (200);
ALTER TABLE pdf_answers MODIFY url TEXT;
ALTER TABLE pdf_answers ADD uuid VARCHAR (50);
CREATE INDEX idx_uuid ON pdf_answers (uuid);