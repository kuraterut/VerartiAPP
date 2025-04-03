package service

import (
	"fmt"
	"verarti/internal/domain"
	"verarti/internal/repository"
	"verarti/models"
)

type TransactionService struct {
	repo *repository.Repository
}

func NewTransactionService(repo *repository.Repository) *TransactionService {
	return &TransactionService{repo: repo}
}

func (s *TransactionService) CreateTransactions(transactions []models.Transaction) error {
	var (
		adminIds, clientIds   []int
		optionIds, productIds []int
	)

	for _, transaction := range transactions {
		adminIds = append(adminIds, transaction.AdminId)
		clientIds = append(clientIds, transaction.ClientId)

		if transaction.TransactionType == domain.TransactionProduct {
			productIds = append(productIds, transaction.UnitId)
		} else if transaction.TransactionType == domain.TransactionOption {
			optionIds = append(optionIds, transaction.UnitId)
		} else {
			return domain.NewErrorResponse(400, fmt.Sprintf("invalid transaction type = %s", transaction.TransactionType))
		}
	}

	if err := s.repo.User.CheckUsersRoles(adminIds, domain.AdminRole); err != nil {
		return err
	}

	if err := s.repo.Client.CheckingClientsExistence(clientIds); err != nil {
		return err
	}

	if err := s.repo.Option.CheckingOptionsExistence(optionIds); err != nil {
		return err
	}

	if err := s.repo.Product.CheckingProductsExistence(productIds); err != nil {
		return err
	}

	return s.repo.Transaction.CreateTransactions(transactions)
}

func (s *TransactionService) GetAllTransactions() ([]models.Transaction, error) {
	return s.repo.Transaction.GetAllTransactions()
}

func (s *TransactionService) GetTransactionById(transactionId int) (models.Transaction, error) {
	return s.repo.Transaction.GetTransactionById(transactionId)
}

func (s *TransactionService) DeleteTransaction(transactionId int) error {
	return s.repo.Transaction.DeleteTransaction(transactionId)
}

func (s *TransactionService) GetTransactionByDate(date string) ([]models.Transaction, error) {
	return s.repo.Transaction.GetTransactionByDate(date)
}

func (s *TransactionService) GetTransactionByDateAndMethod(date, paymentMethod string) ([]models.Transaction, error) {
	return s.repo.Transaction.GetTransactionByDateAndMethod(date, paymentMethod)
}

func (s *TransactionService) GetTransactionByDateAndType(date, transactionType string) ([]models.Transaction, error) {
	return s.repo.Transaction.GetTransactionByDateAndType(date, transactionType)
}

func (s *TransactionService) GetPaymentMethods() []*models.PaymentMethod {
	return s.repo.Transaction.GetPaymentMethods()
}

func (s *TransactionService) GetTransactionTypes() []*models.TransactionType {
	return s.repo.Transaction.GetTransactionTypes()
}
