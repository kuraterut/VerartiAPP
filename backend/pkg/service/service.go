package service

import (
	"verarti/models"
	"verarti/pkg/repository"
)

type Authorization interface {
	CreateUser(user models.Users, roleId int) (int, error)
	GenerateToken(phone, password string) (string, string, error)
	ParseToken(token string) (int, string, error)
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

type Service struct {
	Appointment
	Authorization
	Client
	Feedback
	Resource
	Schedule
	User
}

func NewService(repos *repository.Repository) *Service {
	return &Service{
		Resource:      NewResourceService(repos),
		Authorization: NewAuthService(repos),
	}
}
