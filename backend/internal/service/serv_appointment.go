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

func (s *AppointmentService) CreateAppointment(appointment models.Appointment) (int, error) {
	return s.repo.CreateAppointment(appointment)
}

func (s *AppointmentService) GetAllAppointments() ([]models.Appointment, error) {
	return s.repo.GetAllAppointments()
}

func (s *AppointmentService) GetAppointmentById(appointmentId int) (models.Appointment, error) {
	return s.repo.GetAppointmentById(appointmentId)
}
