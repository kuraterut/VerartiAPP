package service

import (
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

func (s *TransactionService) CreateTransaction(transaction models.Transaction) (int, error) {
	err := s.repo.User.CheckUsersRole(transaction.AdminId, domain.AdminRole)
	if err != nil {
		return 0, err
	}

	err = s.repo.Client.CheckingClientExistence(transaction.ClientId)
	if err != nil {
		return 0, err
	}

	if transaction.TransactionType == domain.TransactionAppointment {
		err = s.repo.Appointment.CheckingAppointmentExistence(transaction.UnitId)
		if err != nil {
			return 0, err
		}
	} else if transaction.TransactionType == domain.TransactionProduct {
		err = s.repo.Product.CheckingProductExistence(transaction.UnitId)
		if err != nil {
			return 0, err
		}
	} else {
		return 0, domain.NewErrorResponse(400, "invalid transaction type")
	}

	return s.repo.CreateTransaction(transaction)
}

func (s *TransactionService) GetAllTransactions() ([]models.Transaction, error) {
	return s.repo.GetAllTransactions()
}

func (s *TransactionService) GetTransactionById(transactionId int) (models.Transaction, error) {
	return s.repo.GetTransactionById(transactionId)
}

func (s *TransactionService) DeleteTransaction(transactionId int) error {
	return s.repo.DeleteTransaction(transactionId)
}
