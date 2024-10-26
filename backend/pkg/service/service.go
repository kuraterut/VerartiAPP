package service

import (
	"verarti/models"
	"verarti/pkg/repository"
)

type Authorization interface {
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
		Resource: NewResourceService(repos),
	}
}
