package models

type Client struct {
	Id         int    `json:"id" db:"id"`
	Name       string `json:"name" binding:"required" db:"name"`
	Surname    string `json:"surname" binding:"required" db:"surname"`
	Patronymic string `json:"patronymic,omitempty" db:"patronymic"`
	Email      string `json:"email,omitempty" db:"email"`
	Phone      string `json:"phone" binding:"required" db:"phone"`
	Comment    string `json:"comment,omitempty" db:"comment"`
	Birthday   string `json:"birthday,omitempty" db:"birthday"`
}

type ClientUpdate struct {
	Name       string `json:"name"`
	Surname    string `json:"surname"`
	Patronymic string `json:"patronymic"`
	Email      string `json:"email"`
	Phone      string `json:"phone"`
	Comment    string `json:"comment"`
	Birthday   string `json:"birthday"`
}
