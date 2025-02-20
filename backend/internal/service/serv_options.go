package service

import (
	"verarti/internal/repository"
	"verarti/models"
)

type OptionService struct {
	repo repository.Option
}

func NewOptionService(repo repository.Option) *OptionService {
	return &OptionService{repo: repo}
}

func (s *OptionService) CreateOption(option models.Option) (int, error) {
	return s.repo.CreateOption(option)
}

func (s *OptionService) GetAllOptions() ([]models.Option, error) {
	return s.repo.GetAllOptions()
}

func (s *OptionService) GetOptionsByMasterId(masterId int) ([]models.Option, error) {
	return s.repo.GetOptionsByMasterId(masterId)
}

func (s *OptionService) GetOptionById(optionId int) (models.Option, error) {
	return s.repo.GetOptionById(optionId)
}

func (s *OptionService) UpdateOption(option models.OptionUpdate, optionId int) error {
	return s.repo.UpdateOption(option, optionId)
}

func (s *OptionService) DeleteOption(optionId int) error {
	return s.repo.DeleteOption(optionId)
}

func (s *OptionService) AddOptionForMaster(masterId, optionId int) (int, error) {
	return s.repo.AddOptionForMaster(masterId, optionId)
}
