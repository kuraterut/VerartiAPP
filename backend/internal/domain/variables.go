package domain

var (
	// Roles
	MasterRole = "MASTER"
	AdminRole  = "ADMIN"

	// Transaction Types
	TransactionProduct = "PRODUCT"
	TransactionOption  = "OPTION"

	PaymentMethods   = []string{"CASH", "CARD"}
	TransactionTypes = []string{"PRODUCT", "OPTION"}

	StatusOK = "OK"
)
