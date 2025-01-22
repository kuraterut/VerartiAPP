package service

import (
	"verarti/internal/repository"
	"verarti/models"
)

type ClientService struct {
	repo repository.Client
}

func NewClientService(repo repository.Client) *ClientService {
	return &ClientService{repo: repo}
}

func (s *ClientService) CreateClient(client models.Client) (int, error) {
	return s.repo.CreateClient(client)
}

func (s *ClientService) GetClientByPhone(phone string) (models.Client, error) {
	return s.repo.GetClientByPhone(phone)
}
