package database

import (
	"fmt"
	"github.com/jmoiron/sqlx"
)

const (
	ResourceTable             = "resource"
	UserTable                 = "users"
	UserResourceTable         = "users_resource"
	RoleTable                 = "role"
	UsersRoleTable            = "users_role"
	ClientTable               = "client"
	OptionTable               = "option"
	UsersOptionTable          = "users_option"
	AdminShiftTable           = "admin_shift"
	MasterShiftTable          = "master_shift"
	MasterScheduleTable       = "master_schedule"
	MasterScheduleOptionTable = "master_schedule_option"
)

type Config struct {
	Host     string
	Port     string
	Username string
	Password string
	DBName   string
	SSLMode  string
}

func NewPostgresDB(cfg Config) (*sqlx.DB, error) {
	db, err := sqlx.Open("postgres", fmt.Sprintf("host=%s port=%s user=%s dbname=%s password=%s sslmode=%s",
		cfg.Host, cfg.Port, cfg.Username, cfg.DBName, cfg.Password, cfg.SSLMode))

	if err != nil {
		return nil, err
	}

	err = db.Ping()
	if err != nil {
		return nil, err
	}

	return db, nil
}
