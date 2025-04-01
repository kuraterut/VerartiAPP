package repository

import (
	"database/sql"
	"errors"
	"fmt"
	"github.com/jmoiron/sqlx"
	"verarti/internal/domain"
	"verarti/models"
	"verarti/pkg/database"
)

type TransactionPostgres struct {
	db *sqlx.DB
}

func NewTransactionPostgres(db *sqlx.DB) *TransactionPostgres {
	return &TransactionPostgres{db: db}
}

func (r *TransactionPostgres) CreateTransaction(transaction models.Transaction) (int, error) {
	var transactionId, transactionTypeId, paymentMethodId int

	getTransactionTypeIdQuery := fmt.Sprintf("SELECT id FROM %s WHERE name = $1", database.TransactionTypeTable)
	err := r.db.Get(&transactionTypeId, getTransactionTypeIdQuery, transaction.TransactionType)
	if err != nil {
		if errors.Is(err, sql.ErrNoRows) {
			return 0, domain.NewErrorResponse(404, fmt.Sprintf("transaction type with name = %s not found", transaction.TransactionType))
		}

		return 0, err
	}

	getPaymentMethodIdQuery := fmt.Sprintf("SELECT id FROM %s WHERE name = $1", database.PaymentMethodTable)
	err = r.db.Get(&paymentMethodId, getPaymentMethodIdQuery, transaction.PaymentMethod)
	if err != nil {
		if errors.Is(err, sql.ErrNoRows) {
			return 0, domain.NewErrorResponse(404, fmt.Sprintf("payment method with name = %s not found", transaction.PaymentMethod))
		}

		return 0, err
	}

	operationName, err := getOperationNameByTransactionType(transaction.TransactionType)
	if err != nil {
		return 0, err
	}

	createTransactionQuery := fmt.Sprintf("INSERT INTO %s (users_id, client_id, %s, "+
		" payment_method_id, transaction_type_id, purchase_amount, count) "+
		" VALUES ($1, $2, $3, $4, $5, $6, $7) RETURNING id", database.TransactionTable, operationName)
	row := r.db.QueryRow(createTransactionQuery, transaction.AdminId, transaction.ClientId, transaction.UnitId,
		paymentMethodId, transactionTypeId, transaction.PurchaseAmount, transaction.Count)
	if err := row.Scan(&transactionId); err != nil {
		return 0, err
	}

	return transactionId, nil
}

func (r *TransactionPostgres) GetAllTransactions() ([]models.Transaction, error) {
	var transactions []models.Transaction

	queryGetAllTransactions := fmt.Sprintf(`SELECT id, users_id, client_id, appointment_id, product_id, 
    payment_method_id, transaction_type_id, purchase_amount, count, 
    TO_CHAR(date_and_time, 'YYYY-MM-DD HH:MM:SS') AS date_and_time FROM %s`, database.TransactionTable)
	rows, err := r.db.Query(queryGetAllTransactions)
	if err != nil {
		if errors.Is(err, sql.ErrNoRows) {
			return []models.Transaction{}, nil
		}

		return nil, err
	}
	defer rows.Close()

	for rows.Next() {
		var (
			appointmentId, productId           sql.NullInt64
			paymentMethodId, transactionTypeId int
			transaction                        models.Transaction
		)

		err = rows.Scan(&transaction.Id, &transaction.AdminId, &transaction.ClientId, &appointmentId, &productId,
			&paymentMethodId, &transactionTypeId, &transaction.PurchaseAmount, &transaction.Count, &transaction.Timestamp)
		if err != nil {
			return nil, domain.NewErrorResponse(500, fmt.Sprintf("error scanning row: %v", err))
		}

		if appointmentId.Valid {
			transaction.UnitId = int(appointmentId.Int64)
		} else if productId.Valid {
			transaction.UnitId = int(productId.Int64)
		} else {
			return nil, domain.NewErrorResponse(500, "unit id not found")
		}

		queryGetPaymentMethod := fmt.Sprintf("SELECT name FROM %s WHERE id = $1", database.PaymentMethodTable)
		err = r.db.Get(&transaction.PaymentMethod, queryGetPaymentMethod, paymentMethodId)
		if err != nil {
			continue
		}

		queryGetTransactionType := fmt.Sprintf("SELECT name FROM %s WHERE id = $1", database.TransactionTypeTable)
		err = r.db.Get(&transaction.TransactionType, queryGetTransactionType, transactionTypeId)
		if err != nil {
			continue
		}

		transactions = append(transactions, transaction)
	}

	if len(transactions) == 0 {
		return []models.Transaction{}, nil
	}

	return transactions, err
}

func (r *TransactionPostgres) GetTransactionById(transactionId int) (models.Transaction, error) {
	var transactions []models.Transaction

	queryGetAllTransactions := fmt.Sprintf(`SELECT id, users_id, client_id, appointment_id, product_id, 
    payment_method_id, transaction_type_id, purchase_amount, count, 
    TO_CHAR(date_and_time, 'YYYY-MM-DD HH:MM:SS') AS date_and_time FROM %s WHERE id = $1`, database.TransactionTable)
	rows, err := r.db.Query(queryGetAllTransactions, transactionId)
	if err != nil {
		if errors.Is(err, sql.ErrNoRows) {
			return models.Transaction{}, nil
		}

		return models.Transaction{}, err
	}
	defer rows.Close()

	for rows.Next() {
		var (
			appointmentId, productId           sql.NullInt64
			paymentMethodId, transactionTypeId int
			transaction                        models.Transaction
		)

		err = rows.Scan(&transaction.Id, &transaction.AdminId, &transaction.ClientId, &appointmentId, &productId,
			&paymentMethodId, &transactionTypeId, &transaction.PurchaseAmount, &transaction.Count, &transaction.Timestamp)
		if err != nil {
			return models.Transaction{}, err
		}

		if appointmentId.Valid {
			transaction.UnitId = int(appointmentId.Int64)
		} else if productId.Valid {
			transaction.UnitId = int(productId.Int64)
		} else {
			return models.Transaction{}, domain.NewErrorResponse(500, "unit id not found")
		}

		queryGetPaymentMethod := fmt.Sprintf("SELECT name FROM %s WHERE id = $1", database.PaymentMethodTable)
		err = r.db.Get(&transaction.PaymentMethod, queryGetPaymentMethod, paymentMethodId)
		if err != nil {
			if errors.Is(err, sql.ErrNoRows) {
				return models.Transaction{}, domain.NewErrorResponse(404, fmt.Sprintf("payment method with id = %d not found", paymentMethodId))
			}

			return models.Transaction{}, err
		}

		queryGetTransactionType := fmt.Sprintf("SELECT name FROM %s WHERE id = $1", database.TransactionTypeTable)
		err = r.db.Get(&transaction.TransactionType, queryGetTransactionType, transactionTypeId)
		if err != nil {
			if errors.Is(err, sql.ErrNoRows) {
				return models.Transaction{}, domain.NewErrorResponse(404, fmt.Sprintf("transaction type with id = %d not found", transactionTypeId))
			}

			return models.Transaction{}, err
		}

		transactions = append(transactions, transaction)
	}

	if len(transactions) == 0 {
		return models.Transaction{}, domain.NewErrorResponse(404, fmt.Sprintf("transaction with id = %d not found", transactionId))
	}

	if len(transactions) > 1 {
		return models.Transaction{}, domain.NewErrorResponse(500, fmt.Sprintf("multiple transactions with id = %d found", transactionId))
	}

	return transactions[0], err
}

func (r *TransactionPostgres) DeleteTransaction(transactionId int) error {
	query := fmt.Sprintf(`DELETE FROM %s WHERE id = $1`, database.TransactionTable)
	_, err := r.db.Exec(query, transactionId)
	if err != nil {
		if errors.Is(err, sql.ErrNoRows) {
			return nil
		}

		return err
	}

	return nil
}

// the method get the column name by transaction type
func getOperationNameByTransactionType(transactionType string) (string, error) {
	switch transactionType {
	case domain.TransactionProduct:
		return "product_id", nil
	case domain.TransactionAppointment:
		return "appointment_id", nil
	default:
		return "", errors.New("invalid transaction type")
	}
}
