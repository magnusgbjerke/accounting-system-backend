CREATE VIEW Transactions AS
SELECT
voucher.id,
voucher.year_id as "year",
posting.date,
posting.account_id,
account.name as "account_name",
posting.amount,
voucher.temp_created_on,
voucher.created_on
FROM voucher
INNER JOIN posting on voucher.id=posting.voucher_id_fk and voucher.year_id=posting.voucher_year_fk
INNER JOIN account on account.id=posting.account_id