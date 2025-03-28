package models

type MasterAppointment struct {
	Id        int      `json:"id" db:"id"`
	Client    Client   `json:"client"`
	Master    Users    `json:"master"`
	Options   []Option `json:"options"`
	Status    string   `json:"status" db:"status"`
	StartTime string   `json:"start_time" db:"start_time"`
	Date      string   `json:"date" db:"date"`
	Comment   string   `json:"comment" db:"comment"`
}

type MasterAppointmentInput struct {
	Id        int    `json:"-"`
	ClientId  int    `json:"client_id" binding:"required"`
	MasterId  int    `json:"master_id" binding:"required"`
	OptionIds []int  `json:"option_ids" binding:"required"`
	StartTime string `json:"start_time" binding:"required"`
	Date      string `json:"date" binding:"required"`
	Comment   string `json:"comment"`
}

type MasterShift struct {
	Id       int    `json:"-" db:"id"`
	MasterId int    `json:"master_id" binding:"required"`
	Date     string `json:"date" binding:"required"`
}

type AdminShift struct {
	Id      int    `json:"-" db:"id"`
	AdminId int    `json:"admin_id" binding:"required"`
	Date    string `json:"date" binding:"required"`
}
