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
