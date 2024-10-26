package service

import (
	"verarti/models"
	"verarti/pkg/repository"
)

type ResourceService struct {
	repo repository.Resource
}

func NewResourceService(repo repository.Resource) *ResourceService {
	return &ResourceService{repo: repo}
}

func (s *ResourceService) Create(resource models.Resource) (int, error) {
	return s.repo.Create(resource)
}

func (s *ResourceService) GetAll() ([]models.Resource, error) {
	return s.repo.GetAll()
}
