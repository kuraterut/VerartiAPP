package models

type Appointment struct {
	Id          int    `json:"id" db:"id"`
	Price       int    `json:"price" db:"price" binding:"required"`
	Name        string `json:"name" db:"name" binding:"required"`
	Description string `json:"description" db:"description" binding:"required"`
	Duration    string `json:"duration" db:"duration" binding:"required"`
}

type AppointmentUpdate struct {
	Price       int    `json:"price,omitempty"`
	Name        string `json:"name,omitempty"`
	Description string `json:"description,omitempty"`
	Duration    string `json:"duration,omitempty"`
}
