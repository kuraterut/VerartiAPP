package models

type Appointment struct {
	Id          int    `json:"-" db:"id"`
	Name        string `json:"name"`
	Description string `json:"description"`
	Price       int    `json:"price"`
	Duration    int    `json:"duration"`
}
