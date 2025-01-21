package service

import (
	"verarti/internal/repository"
	"verarti/models"
)

type ProfileService struct {
	repo repository.Profile
}

func NewProfileService(repo repository.Profile) *ProfileService {
	return &ProfileService{repo: repo}
}

func (s *ProfileService) GetUserInfo(userId int) (models.Users, error) {
	return s.repo.GetUserInfo(userId)
}

func (s *ProfileService) UpdatePhoto(userId int, newPhoto []byte) error {
	return s.repo.UpdatePhoto(userId, newPhoto)
}
