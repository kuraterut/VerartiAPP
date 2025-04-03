package service

import (
	"verarti/internal/domain"
	"verarti/internal/repository"
	"verarti/models"
)

type OptionService struct {
	repo *repository.Repository
}

func NewOptionService(repo *repository.Repository) *OptionService {
	return &OptionService{repo: repo}
}

func (s *OptionService) CreateOption(option models.Option) (int, error) {
	return s.repo.Option.CreateOption(option)
}

func (s *OptionService) GetAllOptions() ([]models.Option, error) {
	return s.repo.Option.GetAllOptions()
}

func (s *OptionService) GetOptionsByMasterId(masterId int) ([]models.Option, error) {
	return s.repo.Option.GetOptionsByMasterId(masterId)
}

func (s *OptionService) GetOptionById(optionId int) (models.Option, error) {
	return s.repo.Option.GetOptionById(optionId)
}

func (s *OptionService) UpdateOption(option models.OptionUpdate, optionId int) error {
	return s.repo.Option.UpdateOption(option, optionId)
}

func (s *OptionService) DeleteOption(optionId int) error {
	exists, err := s.repo.Option.CheckingActiveOptionExistenceForAllUsers(optionId)
	if err != nil {
		return err
	}

	if exists {
		return domain.NewErrorResponse(409, "The option cannot be removed because it is in the active records")
	}

	return s.repo.Option.DeleteOption(optionId)
}

func (s *OptionService) AddOptionForMaster(masterId, optionId int) (int, error) {
	return s.repo.Option.AddOptionForMaster(masterId, optionId)
}

func (s *OptionService) RemoveOptionFromTheMaster(optionId, masterId int) error {
	if err := s.repo.Option.CheckingOptionsExistence([]int{optionId}); err != nil {
		return err
	}

	if err := s.repo.User.CheckUsersRoles([]int{masterId}, domain.MasterRole); err != nil {
		return err
	}

	exists, err := s.repo.Option.CheckingActiveOptionExistenceForMaster(optionId, masterId)
	if err != nil {
		return err
	}

	if exists {
		return domain.NewErrorResponse(409, "The option cannot be removed from the master because it is in the active records")
	}

	return s.repo.Option.RemoveOptionFromTheMaster(optionId, masterId)
}
