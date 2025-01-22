package service

import (
	"verarti/internal/repository"
	"verarti/models"
)

type Authorization interface {
	CreateUser(user models.Users, roleIds []int) (int, error)
	GenerateToken(phone, password, role string) (string, error)
	ParseToken(token string) (int, []string, error)
}

type Appointment interface {
}

type Client interface {
	CreateClient(client models.Client) (int, error)
	GetClientByPhone(phone string) (models.Client, error)
	GetClientById(clientId int) (models.Client, error)
	GetAllClients() ([]models.Client, error)
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

type Profile interface {
	GetUserInfo(userId int) (models.Users, error)
	//UpdateInfo(userId int, info models.Info) error
	//UpdatePassword(userId int, passwords models.UpdatePasswordInput) error
	UpdatePhoto(userId int, newPhoto []byte) error
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
	Profile
}

func NewService(repos *repository.Repository) *Service {
	return &Service{
		Resource:      NewResourceService(repos),
		Authorization: NewAuthService(repos),
		Profile:       NewProfileService(repos),
		Client:        NewClientService(repos),
	}
}
