package service

import (
	"verarti/models"
	"verarti/pkg/repository"
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
