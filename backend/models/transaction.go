package models

type Transaction struct {
	Id              int    `json:"id" db:"id"`
	Timestamp       string `json:"timestamp" db:"date_and_time"`
	TransactionType string `json:"transaction_type" db:"transaction_type" binding:"required"`
	PaymentMethod   string `json:"payment_method" db:"payment_method" binding:"required"`
	PurchaseAmount  int    `json:"purchase_amount" db:"purchase_amount" binding:"required"`
	Count           int    `json:"count" db:"count" binding:"required"`
	UnitId          int    `json:"unit_id" db:"unit_id" binding:"required"`
	AdminId         int    `json:"admin_id" db:"admin_id" binding:"required"`
	ClientId        int    `json:"client_id" db:"client_id" binding:"required"`
}
