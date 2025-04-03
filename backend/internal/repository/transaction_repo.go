package repository

import (
	"database/sql"
	"errors"
	"fmt"
	"github.com/jmoiron/sqlx"
	"strings"
	"verarti/internal/domain"
	"verarti/models"
	"verarti/pkg/database"
)

type TransactionPostgres struct {
	db               *sqlx.DB
	transactionTypes []*models.TransactionType
	paymentMethods   []*models.PaymentMethod
}

func NewTransactionPostgres(db *sqlx.DB) (*TransactionPostgres, error) {
	transactionPostgres := &TransactionPostgres{db: db}

	var err error
	transactionPostgres.transactionTypes, err = transactionPostgres.getTransactionTypesFromPostgres()
	if err != nil {
		return transactionPostgres, err
	}

	transactionPostgres.paymentMethods, err = transactionPostgres.getPaymentMethodsFromPostgres()
	if err != nil {
		return transactionPostgres, err
	}

	return transactionPostgres, nil
}

func (r *TransactionPostgres) getTransactionTypesFromPostgres() ([]*models.TransactionType, error) {
	var transactionTypes []*models.TransactionType

	queryGetTransactionTypes := fmt.Sprintf("SELECT id, name FROM %s", database.TransactionTypeTable)
	rows, err := r.db.Query(queryGetTransactionTypes)
	if err != nil {
		return nil, err
	}
	defer rows.Close()

	for rows.Next() {
		var transactionType models.TransactionType

		err := rows.Scan(&transactionType.Id, &transactionType.Name)
		if err != nil {
			return nil, err
		}

		transactionTypes = append(transactionTypes, &transactionType)
	}

	return transactionTypes, nil
}

func (r *TransactionPostgres) getPaymentMethodsFromPostgres() ([]*models.PaymentMethod, error) {
	var paymentMethods []*models.PaymentMethod

	queryGetPaymentMethods := fmt.Sprintf("SELECT id, name FROM %s", database.PaymentMethodTable)
	rows, err := r.db.Query(queryGetPaymentMethods)
	if err != nil {
		return nil, err
	}
	defer rows.Close()

	for rows.Next() {
		var paymentMethod models.PaymentMethod

		err := rows.Scan(&paymentMethod.Id, &paymentMethod.Name)
		if err != nil {
			return nil, err
		}

		paymentMethods = append(paymentMethods, &paymentMethod)
	}

	return paymentMethods, nil
}

func (r *TransactionPostgres) GetTransactionTypes() []*models.TransactionType {
	return r.transactionTypes
}

func (r *TransactionPostgres) GetPaymentMethods() []*models.PaymentMethod {
	return r.paymentMethods
}

func (r *TransactionPostgres) CreateTransactions(transactions []models.Transaction) error {
	var (
		productPlaceholders []string
		optionPlaceholders  []string
		productQueryArgs    []interface{}
		optionQueryArgs     []interface{}
	)

	updateProducts := make(map[int]int)

	productQuery := fmt.Sprintf("INSERT INTO %s (users_id, client_id, product_id, payment_method,"+
		" transaction_type, purchase_amount, count) VALUES ", database.TransactionTable)
	optionQuery := fmt.Sprintf("INSERT INTO %s (users_id, client_id, option_id, payment_method,"+
		" transaction_type, purchase_amount, count) VALUES ", database.TransactionTable)

	productArgIdx, optionArgIdx := 1, 1
	for _, transaction := range transactions {
		args := []interface{}{
			transaction.AdminId,
			transaction.ClientId,
			transaction.UnitId,
			transaction.PaymentMethod,
			transaction.TransactionType,
			transaction.PurchaseAmount,
			transaction.Count,
		}

		if transaction.TransactionType == domain.TransactionProduct {
			productPlaceholders = append(productPlaceholders, fmt.Sprintf("($%d, $%d, $%d, $%d, $%d, $%d, $%d)",
				productArgIdx, productArgIdx+1, productArgIdx+2, productArgIdx+3, productArgIdx+4, productArgIdx+5, productArgIdx+6))
			productQueryArgs = append(productQueryArgs, args...)

			productArgIdx += 7
			updateProducts[transaction.UnitId] += transaction.Count
		} else if transaction.TransactionType == domain.TransactionOption {
			optionPlaceholders = append(optionPlaceholders, fmt.Sprintf("($%d, $%d, $%d, $%d, $%d, $%d, $%d)",
				optionArgIdx, optionArgIdx+1, optionArgIdx+2, optionArgIdx+3, optionArgIdx+4, optionArgIdx+5, optionArgIdx+6))
			optionQueryArgs = append(optionQueryArgs, args...)

			optionArgIdx += 7
		} else {
			return domain.NewErrorResponse(400, fmt.Sprintf("invalid transaction type: %s", transaction.TransactionType))
		}
	}

	tx, err := r.db.Begin()
	if err != nil {
		return err
	}
	defer tx.Rollback()

	if len(productPlaceholders) > 0 {
		finalProductQuery := productQuery + strings.Join(productPlaceholders, ",")
		_, err := tx.Exec(finalProductQuery, productQueryArgs...)
		if err != nil {
			return err
		}
	}

	if len(optionPlaceholders) > 0 {
		finalOptionQuery := optionQuery + strings.Join(optionPlaceholders, ",")
		_, err = tx.Exec(finalOptionQuery, optionQueryArgs...)
		if err != nil {
			return domain.NewErrorResponse(500, fmt.Sprintf("failed to create transactions: %v", err))
		}
	}

	for productId, count := range updateProducts {
		queryUpdateProducts := fmt.Sprintf("UPDATE %s SET count = count - $1 WHERE id = $2", database.ProductTable)
		_, err = tx.Exec(queryUpdateProducts, count, productId)
		if err != nil {
			return domain.NewErrorResponse(500, fmt.Sprintf("failed to update product count: %v", err))
		}
	}

	return tx.Commit()
}

func (r *TransactionPostgres) GetAllTransactions() ([]models.Transaction, error) {
	var transactions []models.Transaction

	queryGetAllTransactions := fmt.Sprintf(`SELECT id, users_id, client_id, option_id, product_id, 
    payment_method, transaction_type, purchase_amount, count, 
    TO_CHAR(date_and_time, 'YYYY-MM-DD HH24:MI:SS') AS date_and_time FROM %s`, database.TransactionTable)
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
			optionId, productId sql.NullInt64
			transaction         models.Transaction
		)

		err = rows.Scan(&transaction.Id, &transaction.AdminId, &transaction.ClientId, &optionId, &productId,
			&transaction.PaymentMethod, &transaction.TransactionType, &transaction.PurchaseAmount, &transaction.Count, &transaction.Timestamp)
		if err != nil {
			return nil, domain.NewErrorResponse(500, fmt.Sprintf("error scanning row: %v", err))
		}

		if optionId.Valid {
			transaction.UnitId = int(optionId.Int64)
		} else if productId.Valid {
			transaction.UnitId = int(productId.Int64)
		} else {
			return nil, domain.NewErrorResponse(500, "unit id not found")
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

	queryGetAllTransactions := fmt.Sprintf(`SELECT id, users_id, client_id, option_id, product_id, 
    payment_method, transaction_type, purchase_amount, count, 
    TO_CHAR(date_and_time, 'YYYY-MM-DD HH24:MI:SS') AS date_and_time FROM %s WHERE id = $1`, database.TransactionTable)
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
			optionId, productId sql.NullInt64
			transaction         models.Transaction
		)

		err = rows.Scan(&transaction.Id, &transaction.AdminId, &transaction.ClientId, &optionId, &productId,
			&transaction.PaymentMethod, &transaction.TransactionType, &transaction.PurchaseAmount, &transaction.Count, &transaction.Timestamp)
		if err != nil {
			return models.Transaction{}, err
		}

		if optionId.Valid {
			transaction.UnitId = int(optionId.Int64)
		} else if productId.Valid {
			transaction.UnitId = int(productId.Int64)
		} else {
			return models.Transaction{}, domain.NewErrorResponse(500, "unit id not found")
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
	transaction, err := r.GetTransactionById(transactionId)
	if err != nil {
		if errors.Is(err, sql.ErrNoRows) {
			return nil
		}

		return err
	}

	tx, err := r.db.Begin()
	if err != nil {
		return err
	}
	defer tx.Rollback()

	query := fmt.Sprintf(`DELETE FROM %s WHERE id = $1`, database.TransactionTable)
	_, err = tx.Exec(query, transactionId)
	if err != nil {
		if errors.Is(err, sql.ErrNoRows) {
			return tx.Commit()
		}

		return err
	}

	if transaction.TransactionType == domain.TransactionProduct {
		queryUpdateProducts := fmt.Sprintf("UPDATE %s SET count = count + $1 WHERE id = $2", database.ProductTable)
		_, err = tx.Exec(queryUpdateProducts, transaction.Count, transaction.UnitId)
		if err != nil {
			return domain.NewErrorResponse(500, fmt.Sprintf("failed to update product count: %v", err))
		}
	}

	return tx.Commit()
}

func (r *TransactionPostgres) GetTransactionByDate(date string) ([]models.Transaction, error) {
	var transactions []models.Transaction

	queryGetAllTransactions := fmt.Sprintf(`SELECT id, users_id, client_id, option_id, product_id, 
    payment_method, transaction_type, purchase_amount, count, 
    TO_CHAR(date_and_time, 'YYYY-MM-DD HH24:MI:SS') AS date_and_time FROM %s WHERE DATE(date_and_time) = $1`, database.TransactionTable)
	rows, err := r.db.Query(queryGetAllTransactions, date)
	if err != nil {
		if errors.Is(err, sql.ErrNoRows) {
			return []models.Transaction{}, nil
		}

		return nil, err
	}
	defer rows.Close()

	for rows.Next() {
		var (
			optionId, productId sql.NullInt64
			transaction         models.Transaction
		)

		err = rows.Scan(&transaction.Id, &transaction.AdminId, &transaction.ClientId, &optionId, &productId,
			&transaction.PaymentMethod, &transaction.TransactionType, &transaction.PurchaseAmount, &transaction.Count, &transaction.Timestamp)
		if err != nil {
			return nil, domain.NewErrorResponse(500, fmt.Sprintf("error scanning row: %v", err))
		}

		if optionId.Valid {
			transaction.UnitId = int(optionId.Int64)
		} else if productId.Valid {
			transaction.UnitId = int(productId.Int64)
		} else {
			return nil, domain.NewErrorResponse(500, "unit id not found")
		}

		transactions = append(transactions, transaction)
	}

	if len(transactions) == 0 {
		return []models.Transaction{}, nil
	}

	return transactions, err
}

func (r *TransactionPostgres) GetTransactionByDateAndMethod(date, paymentMethod string) ([]models.Transaction, error) {
	var transactions []models.Transaction

	queryGetAllTransactions := fmt.Sprintf(`SELECT id, users_id, client_id, option_id, product_id, 
    payment_method, transaction_type, purchase_amount, count, 
    TO_CHAR(date_and_time, 'YYYY-MM-DD HH24:MI:SS') AS date_and_time FROM %s WHERE DATE(date_and_time) = $1 AND payment_method = $2`, database.TransactionTable)
	rows, err := r.db.Query(queryGetAllTransactions, date, paymentMethod)
	if err != nil {
		if errors.Is(err, sql.ErrNoRows) {
			return []models.Transaction{}, nil
		}

		return nil, err
	}
	defer rows.Close()

	for rows.Next() {
		var (
			optionId, productId sql.NullInt64
			transaction         models.Transaction
		)

		err = rows.Scan(&transaction.Id, &transaction.AdminId, &transaction.ClientId, &optionId, &productId,
			&transaction.PaymentMethod, &transaction.TransactionType, &transaction.PurchaseAmount, &transaction.Count, &transaction.Timestamp)
		if err != nil {
			return nil, domain.NewErrorResponse(500, fmt.Sprintf("error scanning row: %v", err))
		}

		if optionId.Valid {
			transaction.UnitId = int(optionId.Int64)
		} else if productId.Valid {
			transaction.UnitId = int(productId.Int64)
		} else {
			return nil, domain.NewErrorResponse(500, "unit id not found")
		}

		transactions = append(transactions, transaction)
	}

	if len(transactions) == 0 {
		return []models.Transaction{}, nil
	}

	return transactions, err
}

func (r *TransactionPostgres) GetTransactionByDateAndType(date, transactionType string) ([]models.Transaction, error) {
	var transactions []models.Transaction

	queryGetAllTransactions := fmt.Sprintf(`SELECT id, users_id, client_id, option_id, product_id, 
    payment_method, transaction_type, purchase_amount, count, 
    TO_CHAR(date_and_time, 'YYYY-MM-DD HH24:MI:SS') AS date_and_time FROM %s WHERE DATE(date_and_time) = $1 AND transaction_type = $2`, database.TransactionTable)
	rows, err := r.db.Query(queryGetAllTransactions, date, transactionType)
	if err != nil {
		if errors.Is(err, sql.ErrNoRows) {
			return []models.Transaction{}, nil
		}

		return nil, err
	}
	defer rows.Close()

	for rows.Next() {
		var (
			optionId, productId sql.NullInt64
			transaction         models.Transaction
		)

		err = rows.Scan(&transaction.Id, &transaction.AdminId, &transaction.ClientId, &optionId, &productId,
			&transaction.PaymentMethod, &transaction.TransactionType, &transaction.PurchaseAmount, &transaction.Count, &transaction.Timestamp)
		if err != nil {
			return nil, domain.NewErrorResponse(500, fmt.Sprintf("error scanning row: %v", err))
		}

		if optionId.Valid {
			transaction.UnitId = int(optionId.Int64)
		} else if productId.Valid {
			transaction.UnitId = int(productId.Int64)
		} else {
			return nil, domain.NewErrorResponse(500, "unit id not found")
		}

		transactions = append(transactions, transaction)
	}

	if len(transactions) == 0 {
		return []models.Transaction{}, nil
	}

	return transactions, err
}
