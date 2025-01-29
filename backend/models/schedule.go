package models

import "time"

type MasterSchedule struct {
	Id          int         `json:"-" db:"id"`
	Client      Client      `json:"client"`
	Master      Users       `json:"master"`
	Appointment Appointment `json:"appointment"`
	Status      string      `json:"status"`
	Start       time.Time   `json:"start"`
	End         time.Time   `json:"end"`
}

type MasterScheduleInput struct {
	Id            int    `json:"-" db:"id"`
	ClientId      int    `json:"client_id" binding:"required"`
	MasterId      int    `json:"master_id" binding:"required"`
	AppointmentId int    `json:"appointment_id" binding:"required"`
	StartTime     string `json:"start_time" binding:"required"`
	Day           string `json:"day" binding:"required"`
}

type MasterShift struct {
	Id       int    `json:"-" db:"id"`
	MasterId int    `json:"master_id" binding:"required"`
	Day      string `json:"day" binding:"required"`
}

type AdminShift struct {
	Id      int    `json:"-" db:"id"`
	AdminId int    `json:"admin_id" binding:"required"`
	Day     string `json:"day" binding:"required"`
}
