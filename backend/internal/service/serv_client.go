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
