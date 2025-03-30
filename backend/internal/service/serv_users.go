package service

import (
	"verarti/internal/repository"
	"verarti/models"
)

type UserService struct {
	repo repository.User
}

func NewUserService(repo repository.User) *UserService {
	return &UserService{repo: repo}
}

func (s *UserService) GetAllMasters() ([]models.Users, error) {
	return s.repo.GetAllMasters()
}

func (s *UserService) GetMasterById(masterId int) (models.Users, error) {
	return s.repo.GetMasterById(masterId)
}

func (s *UserService) GetAllAdmins() ([]models.Users, error) {
	return s.repo.GetAllAdmins()
}

func (s *UserService) GetAdminById(masterId int) (models.Users, error) {
	return s.repo.GetAdminById(masterId)
}

func (s *UserService) DeleteUser(userId int) error {
	return s.repo.DeleteUser(userId)
}
