package service

import (
	"verarti/internal/domain"
	"verarti/internal/repository"
	"verarti/models"
)

type UserService struct {
	repo *repository.Repository
}

func NewUserService(repo *repository.Repository) *UserService {
	return &UserService{repo: repo}
}

func (s *UserService) GetAllMasters() ([]models.Users, error) {
	return s.repo.User.GetAllMasters()
}

func (s *UserService) GetMasterById(masterId int) (models.Users, error) {
	return s.repo.User.GetMasterById(masterId)
}

func (s *UserService) GetAllAdmins() ([]models.Users, error) {
	return s.repo.User.GetAllAdmins()
}

func (s *UserService) GetAdminById(masterId int) (models.Users, error) {
	return s.repo.User.GetAdminById(masterId)
}

func (s *UserService) DeleteUser(userId int) error {
	exists, err := s.repo.Appointment.CheckingActiveAppointmentExistenceByMasterId(userId)
	if err != nil {
		return err
	}

	if exists {
		return domain.NewErrorResponse(409, "The master cannot be deleted because he has active records")
	}

	return s.repo.User.DeleteUser(userId)
}

func (s *UserService) GetUserByPhone(phone string) (models.Users, error) {
	return s.repo.User.GetUserByPhone(phone)
}
