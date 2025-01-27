package repository

import (
	"github.com/jmoiron/sqlx"
	"github.com/minio/minio-go/v6"
	"verarti/models"
)

type Authorization interface {
	CreateUser(user models.Users, roleIds []int) (int, error)
	GetUser(phone, password string) (models.Users, error)
}

type Appointment interface {
	CreateAppointment(appointment models.Appointment) (int, error)
	GetAllAppointments() ([]models.Appointment, error)
	GetAppointmentById(appointmentId int) (models.Appointment, error)
	UpdateAppointment(appointment models.AppointmentUpdate, appointmentId int) error
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
}

type User interface {
	GetAllMasters() ([]models.Users, error)
	GetMasterById(masterId int) (models.Users, error)
	GetAllAdmins() ([]models.Users, error)
	GetAdminById(masterId int) (models.Users, error)
	GetDirector() (models.Users, error)
	DeleteUser(userId int) error
}

type Profile interface {
	GetUserInfo(userId int) (models.Users, error)
	UpdatePhoto(userId int, newPhoto []byte) error
}

type Repository struct {
	Appointment
	Authorization
	Client
	Feedback
	Resource
	Schedule
	User
	Profile
}

func NewRepository(db *sqlx.DB, minio *minio.Client) *Repository {
	return &Repository{
		Resource:      NewResourcePostgres(db),
		Authorization: NewAuthPostgres(db),
		Profile:       NewProfilePostgres(db),
		Client:        NewClientPostgres(db),
		User:          NewUserPostgres(db),
		Appointment:   NewAppointmentPostgres(db),
	}
}
