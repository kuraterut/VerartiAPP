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
}

type Schedule interface {
}

type User interface {
}

type Repository struct {
	Appointment
	Authorization
	Client
	Feedback
	Resource
	Schedule
	User
}

func NewRepository(db *sqlx.DB) *Repository {
	return &Repository{
		Resource:      NewResourcePostgres(db),
		Authorization: NewAuthPostgres(db),
	}
}
