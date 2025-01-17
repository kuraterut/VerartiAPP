package repository

import (
	"github.com/jmoiron/sqlx"
	"verarti/models"
)

type Authorization interface {
	CreateUser(user models.Users, roleId int) (int, error)
	GetUser(phone, password string) (models.Users, error)
}

type Appointment interface {
}

type Client interface {
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

func NewRepository(db *sqlx.DB) *Repository {
	return &Repository{
		Resource:      NewResourcePostgres(db),
		Authorization: NewAuthPostgres(db),
		Profile:       NewProfilePostgres(db),
	}
}
