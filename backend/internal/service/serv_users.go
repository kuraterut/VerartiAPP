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
