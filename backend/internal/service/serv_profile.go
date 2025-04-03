package service

import (
	"verarti/internal/repository"
	"verarti/models"
)

type ProfileService struct {
	repo *repository.Repository
}

func NewProfileService(repo *repository.Repository) *ProfileService {
	return &ProfileService{repo: repo}
}

func (s *ProfileService) GetUserInfo(userId int) (models.Users, error) {
	return s.repo.GetUserInfo(userId)
}

func (s *ProfileService) UpdatePhoto(userId int, newPhoto []byte) error {
	return s.repo.UpdatePhoto(userId, newPhoto)
}

func (s *ProfileService) UpdateInfo(userId int, info models.UpdateInfo) error {
	return s.repo.UpdateInfo(userId, info)
}

func (s *ProfileService) UpdatePassword(userId int, passwords models.UpdatePasswordInput) error {
	return s.repo.UpdatePassword(userId, generatePasswordHash(passwords.NewPassword), generatePasswordHash(passwords.OldPassword))
}
