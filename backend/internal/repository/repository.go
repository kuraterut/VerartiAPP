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

type Option interface {
	CreateOption(option models.Option) (int, error)
	GetAllOptions() ([]models.Option, error)
	GetOptionsByMasterId(masterId int) ([]models.Option, error)
	GetOptionById(optionId int) (models.Option, error)
	UpdateOption(option models.OptionUpdate, optionId int) error
	DeleteOption(optionId int) error
	AddOptionForMaster(masterId, optionId int) (int, error)
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

type Product interface {
	Create(product models.Product) (int, error)
	GetAll() ([]models.Product, error)
	GetById(productId int) (models.Product, error)
	UpdateProduct(productId int, newProduct models.ProductUpdate) error
	DeleteProduct(productId int) error
}

type Appointment interface {
	PutAdminToDate(adminShift models.AdminShift) error
	PutMasterToDate(masterShift models.MasterShift) error
	GetAdminByDate(date string) (models.Users, error)
	GetAllMastersByDate(date string, isAppointed bool) ([]models.Users, error)
	CreateAppointment(appointment models.MasterAppointmentInput) (int, error)
	GetAppointmentByClientId(clientId int) ([]models.MasterAppointment, error)
	GetAllAppointmentsByDate(date string) ([]models.MasterAppointment, error)
	GetAppointmentById(appointmentId int) (models.MasterAppointment, error)
	DeleteAppointmentById(appointmentId int) error
	UpdateAppointmentById(appointmentId int, input models.MasterAppointmentUpdate) error
}

type User interface {
	GetAllMasters() ([]models.Users, error)
	GetMasterById(masterId int) (models.Users, error)
	GetAllAdmins() ([]models.Users, error)
	GetAdminById(masterId int) (models.Users, error)
	DeleteUser(userId int) error
}

type Profile interface {
	GetUserInfo(userId int) (models.Users, error)
	UpdatePhoto(userId int, newPhoto []byte) error
}

type Repository struct {
	Option
	Authorization
	Client
	Feedback
	Product
	Appointment
	User
	Profile
}

func NewRepository(db *sqlx.DB, minio *minio.Client) *Repository {
	user := NewUserPostgres(db)
	client := NewClientPostgres(db)

	return &Repository{
		Product:       NewProductPostgres(db),
		Authorization: NewAuthPostgres(db),
		Profile:       NewProfilePostgres(db),
		Client:        client,
		User:          user,
		Option:        NewOptionPostgres(db),
		Appointment:   NewAppointmentPostgres(db, user, client),
	}
}
