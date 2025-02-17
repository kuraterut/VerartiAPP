package service

import (
	"verarti/internal/repository"
	"verarti/models"
)

type Authorization interface {
	CreateUser(user models.Users, roleIds []int) (int, error)
	GenerateToken(phone, password, role string) (string, error)
	ParseToken(token string) (int, string, error)
}

type Appointment interface {
	CreateAppointment(appointment models.Appointment) (int, error)
	GetAllAppointments() ([]models.Appointment, error)
	GetAppointmentById(appointmentId int) (models.Appointment, error)
	UpdateAppointment(appointment models.AppointmentUpdate, appointmentId int) error
	DeleteAppointment(appointmentId int) error
	AddAppointmentForMaster(masterId, appointmentId int) (int, error)
}

type Client interface {
	CreateClient(client models.Client) (int, error)
	GetClientByPhone(phone string) (models.Client, error)
	GetClientById(clientId int) (models.Client, error)
	GetAllClients() ([]models.Client, error)
	UpdateClient(clientId int, input models.Client) error
}

type Feedback interface {
}

type Resource interface {
	Create(resource models.Resource) (int, error)
	GetAll() ([]models.Resource, error)
	GetById(resourceId int) (models.Resource, error)
	GetByMasterId(masterId int) ([]models.Resource, error)
	Add(masterId, resourceId int) (int, error)
}

type Schedule interface {
	PutAdminToDate(adminShift models.AdminShift) error
	PutMasterToDate(masterShift models.MasterShift) error
	GetAdminByDate(date string) (models.Users, error)
	GetAllMastersByDate(date string, isAppointed bool) ([]models.Users, error)
	CreateSchedule(schedule models.MasterScheduleInput) (int, error)
	GetScheduleByClientId(clientId int) ([]models.MasterSchedule, error)
}

type Profile interface {
	GetUserInfo(userId int) (models.Users, error)
	//UpdateInfo(userId int, info models.Info) error
	//UpdatePassword(userId int, passwords models.UpdatePasswordInput) error
	UpdatePhoto(userId int, newPhoto []byte) error
}

type User interface {
	GetAllMasters() ([]models.Users, error)
	GetMasterById(masterId int) (models.Users, error)
	GetAllAdmins() ([]models.Users, error)
	GetAdminById(masterId int) (models.Users, error)
	GetDirector() (models.Users, error)
	DeleteUser(userId int) error
}

type Service struct {
	Appointment
	Authorization
	Client
	Feedback
	Resource
	Schedule
	User
	Profile
}

func NewService(repos *repository.Repository) *Service {
	return &Service{
		Resource:      NewResourceService(repos),
		Authorization: NewAuthService(repos),
		Profile:       NewProfileService(repos),
		Client:        NewClientService(repos),
		User:          NewUserService(repos),
		Appointment:   NewAppointmentService(repos),
		Schedule:      NewScheduleService(repos),
	}
}
