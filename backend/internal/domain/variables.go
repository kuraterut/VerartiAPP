package domain

var (
	// Roles
	MasterRole = "MASTER"
	AdminRole  = "ADMIN"

	// Transaction Types
	TransactionProduct     = "PRODUCT"
	TransactionAppointment = "APPOINTMENT"

	PaymentMethods   = []string{"CASH", "CARD"}
	TransactionTypes = []string{"PRODUCT", "APPOINTMENT"}

	StatusOK = "OK"
)
