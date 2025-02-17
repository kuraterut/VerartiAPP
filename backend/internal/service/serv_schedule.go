package service

import (
	"verarti/internal/repository"
	"verarti/models"
)

type ScheduleService struct {
	repo repository.Schedule
}

func NewScheduleService(repo repository.Schedule) *ScheduleService {
	return &ScheduleService{repo: repo}
}

func (s *ScheduleService) PutAdminToDate(adminShift models.AdminShift) error {
	return s.repo.PutAdminToDate(adminShift)
}

func (s *ScheduleService) PutMasterToDate(masterShift models.MasterShift) error {
	return s.repo.PutMasterToDate(masterShift)
}

func (s *ScheduleService) GetAdminByDate(date string) (models.Users, error) {
	return s.repo.GetAdminByDate(date)
}

func (s *ScheduleService) GetAllMastersByDate(date string, isAppointed bool) ([]models.Users, error) {
	return s.repo.GetAllMastersByDate(date, isAppointed)
}

func (s *ScheduleService) CreateSchedule(schedule models.MasterScheduleInput) (int, error) {
	return s.repo.CreateSchedule(schedule)
}

func (s *ScheduleService) GetScheduleByClientId(clientId int) ([]models.MasterSchedule, error) {
	return s.repo.GetScheduleByClientId(clientId)
}
