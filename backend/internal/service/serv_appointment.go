package service

import (
	"verarti/internal/repository"
	"verarti/models"
)

type AppointmentService struct {
	repo repository.Appointment
}

func NewAppointmentService(repo repository.Appointment) *AppointmentService {
	return &AppointmentService{repo: repo}
}

func (s *AppointmentService) PutAdminToDate(adminShift models.AdminShift) error {
	return s.repo.PutAdminToDate(adminShift)
}

func (s *AppointmentService) PutMasterToDate(masterShift models.MasterShift) error {
	return s.repo.PutMasterToDate(masterShift)
}

func (s *AppointmentService) GetAdminByDate(date string) (models.Users, error) {
	return s.repo.GetAdminByDate(date)
}

func (s *AppointmentService) GetAllMastersByDate(date string, isAppointed bool) ([]models.Users, error) {
	return s.repo.GetAllMastersByDate(date, isAppointed)
}

func (s *AppointmentService) CreateAppointment(appointment models.MasterAppointmentInput) (int, error) {
	return s.repo.CreateAppointment(appointment)
}

func (s *AppointmentService) GetAppointmentByClientId(clientId int) ([]models.MasterAppointment, error) {
	return s.repo.GetAppointmentByClientId(clientId)
}
