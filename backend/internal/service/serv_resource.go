package service

import (
	"verarti/internal/repository"
	"verarti/models"
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

func (s *ResourceService) GetById(resourceId int) (models.Resource, error) {
	return s.repo.GetById(resourceId)
}

func (s *ResourceService) GetByMasterId(masterId int) ([]models.Resource, error) {
	return s.repo.GetByMasterId(masterId)
}

func (s *ResourceService) Add(masterId, resourceId int) (int, error) {
	return s.repo.Add(masterId, resourceId)
}
