package models

type DaySchedule struct {
	Masters []Users `json:"masters"`
	Admin   *Users  `json:"admin"`
	Date    string  `json:"date"`
}
