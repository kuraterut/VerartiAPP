package service

import (
	"fmt"
	"time"
	"verarti/internal/domain"
	"verarti/internal/repository"
	"verarti/models"
)

type AppointmentService struct {
	repo *repository.Repository
}

func NewAppointmentService(repo *repository.Repository) *AppointmentService {
	return &AppointmentService{repo: repo}
}

func (s *AppointmentService) PutAdminToDate(adminShift models.AdminShift) error {
	if err := s.repo.User.CheckUsersRoles([]int{adminShift.AdminId}, domain.AdminRole); err != nil {
		return err
	}

	return s.repo.Appointment.PutAdminToDate(adminShift)
}

func (s *AppointmentService) PutMasterToDate(masterShift models.MasterShift) error {
	if err := s.repo.User.CheckUsersRoles([]int{masterShift.MasterId}, domain.MasterRole); err != nil {
		return err
	}

	return s.repo.Appointment.PutMasterToDate(masterShift)
}

func (s *AppointmentService) GetAdminByDate(date string) (models.Users, error) {
	return s.repo.Appointment.GetAdminByDate(date)
}

func (s *AppointmentService) GetAllMastersByDate(date string, isAppointed bool) ([]models.Users, error) {
	return s.repo.Appointment.GetAllMastersByDate(date, isAppointed)
}

func (s *AppointmentService) CreateAppointment(appointment models.MasterAppointmentInput) (int, error) {
	if len(appointment.OptionIds) == 0 {
		return 0, domain.NewErrorResponse(400, "empty option ids")
	}

	if err := s.repo.User.CheckUsersRoles([]int{appointment.MasterId}, domain.MasterRole); err != nil {
		return 0, err
	}

	if err := s.repo.Client.CheckingClientsExistence([]int{appointment.ClientId}); err != nil {
		return 0, err
	}

	return s.repo.Appointment.CreateAppointment(appointment)
}

func (s *AppointmentService) GetAppointmentByClientId(clientId int) ([]models.MasterAppointment, error) {
	return s.repo.Appointment.GetAppointmentByClientId(clientId)
}

func (s *AppointmentService) GetAllAppointmentsByDate(date string) ([]models.MasterAppointment, error) {
	return s.repo.Appointment.GetAllAppointmentsByDate(date)
}

func (s *AppointmentService) GetAppointmentById(appointmentId int) (models.MasterAppointment, error) {
	return s.repo.Appointment.GetAppointmentById(appointmentId)
}

func (s *AppointmentService) DeleteAppointmentById(appointmentId int) error {
	return s.repo.Appointment.DeleteAppointmentById(appointmentId)
}

func (s *AppointmentService) UpdateAppointmentById(appointmentId int, input models.MasterAppointmentUpdate) error {
	return s.repo.Appointment.UpdateAppointmentById(appointmentId, input)
}

func (s *AppointmentService) GetMonthlySchedule(year, month int) ([]models.DaySchedule, error) {
	numberOfDays, err := daysInMonth(year, month)
	if err != nil {
		return nil, err
	}

	schedules := make([]models.DaySchedule, numberOfDays)

	for day := 1; day <= numberOfDays; day++ {
		date := time.Date(year, time.Month(month), day, 0, 0, 0, 0, time.Local)
		schedules[day-1] = models.DaySchedule{Date: date.Format(time.DateOnly)}
	}

	return s.repo.Appointment.GetMonthlySchedule(schedules)
}

func (s *AppointmentService) CancelMasterEntryForDate(masterId int, date string) error {
	if err := s.repo.User.CheckUsersRoles([]int{masterId}, domain.MasterRole); err != nil {
		return err
	}

	return s.repo.Appointment.CancelMasterEntryForDate(masterId, date)
}

func daysInMonth(year int, monthNumber int) (int, error) {
	if monthNumber < 1 || monthNumber > 12 {
		return 0, fmt.Errorf("invalid month number: %d (must be 1-12)", monthNumber)
	}

	month := time.Month(monthNumber)
	return time.Date(year, month+1, 0, 0, 0, 0, 0, time.UTC).Day(), nil
}
